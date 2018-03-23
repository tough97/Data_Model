package com.ynjs.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ynjs.data.excp.IllegalJSONFormatException;
import com.ynjs.data.excp.TreeNodeConflictException;
import com.ynjs.data.excp.TreeNodeInstanciationException;
import com.ynjs.data.listener.NodeListener;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Node extends TreeElement {

    private static UniqueIDGenerator idGenerator;

    public static void setIdGenerator(final UniqueIDGenerator idGenerator) {
        Node.idGenerator = idGenerator;
    }

    public static UniqueIDGenerator getIdGenerator() {
        return idGenerator;
    }

    private static final String CHILDREN = "sub-node";

    private NodeListener nodelistener = null;
    private ConcurrentHashMap<String, TreeElement> childrenMap = new ConcurrentHashMap<>();

    protected Node(final String id, final String name) {
        super(id, name);
    }

    protected Node(final ObjectNode node, final int depth) throws IllegalJSONFormatException, TreeNodeConflictException {
        super(node, depth);
        final JsonNode typeNode = node.get(TreeElement.NODE_TYPE);
        if (typeNode == null || typeNode.isNull()) {
            throw new IllegalJSONFormatException("Node type information missing");
        }
        if (!typeNode.asText().equals(TreeElement.NODE)) {
            throw new IllegalJSONFormatException("Node type = " + typeNode.asText());
        }
        final JsonNode childNodes = node.get(CHILDREN);
        if (childNodes != null) {
            if (!childNodes.isArray()) {
                throw new IllegalJSONFormatException("Node children should be array");
            }

            for (final JsonNode child : childNodes) {
                final JsonNode childTypeNode = child.get(TreeElement.NODE_TYPE);
                if (childTypeNode != null && !childTypeNode.isNull()) {
                    final TreeElement childNode = childTypeNode.asText().equals(TreeElement.NODE) ?
                            new Node(TreeElement.toObjectNode(child), depth + 1) : childTypeNode.asText().equals(TreeElement.LEAF) ?
                            new Leaf(TreeElement.toObjectNode(child), depth + 1) : null;
                    addChild(childNode);
                }
            }
        }
    }

    protected Node addChild(final TreeElement child) throws TreeNodeConflictException {
        child.setParent(this);
        childrenMap.put(child.getId(), child);
        if (nodelistener != null) {
            nodelistener.onChildAdded(this, child);
        }
        child.setDepth(this.getDepth() + 1);
        updated();
        return this;
    }

    public Node setNodeListener(final NodeListener listener) {
        this.nodelistener = listener;
        return this;
    }

    public NodeListener getNodelistener() {
        return nodelistener;
    }

    protected <Element extends TreeElement> Element createChild(
            final String name, final Class<? extends Element> typeClass) throws TreeNodeInstanciationException {
        final String id = idGenerator == null ? UUID.randomUUID().toString() : idGenerator.generateUniqueID();
        try {
            final Element ret = typeClass.getDeclaredConstructor(String.class, String.class).newInstance(id, name);
            addChild(ret);
            return ret;
        } catch (final Exception e) {
            throw new TreeNodeInstanciationException(e);
        }
    }

    protected TreeElement removeChild(final String id) {
        return childrenMap.remove(id);
    }

    public TreeElement getChild(final String id) {
        return childrenMap.get(id);
    }

    public Node createChildNode(final String name) throws TreeNodeInstanciationException {
        return createChild(name, Node.class);
    }

    public Leaf createChildLeaf(final String name) throws TreeNodeInstanciationException {
        return createChild(name, Leaf.class);
    }

    @Override
    protected ObjectNode toJSONObject() {
        final ObjectNode ret = super.toJSONObject();
        if (childrenMap.size() > 0) {
            final ArrayNode childrenArray = ret.putArray(CHILDREN);
            for (final TreeElement child : childrenMap.values()) {
                childrenArray.add(child.toJSONObject());
            }
        }
        return ret;
    }

    public static void main(String[] args) throws IllegalJSONFormatException, TreeNodeInstanciationException, TreeNodeConflictException {
        final Node root = new Node(UUID.randomUUID().toString(), "root");
        root.setProperty("a", new PropertyValue().setValue("a"));
        root.setProperty("b", new PropertyValue().setValue(3.14f));
        root.setProperty("c", new PropertyValue().setValue(new java.util.Date(System.currentTimeMillis())));
        root.setProperty("d", new PropertyValue().setValue(System.currentTimeMillis()));
//
        final Node c1 = root.createChildNode("c1");
        c1.setProperty("a", new PropertyValue().setValue("a"));
        c1.setProperty("b", new PropertyValue().setValue(3.14f));
        c1.setProperty("c", new PropertyValue().setValue(new java.util.Date(System.currentTimeMillis())));
        c1.setProperty("d", new PropertyValue().setValue(System.currentTimeMillis()));
//
        final Leaf l1 = root.createChildLeaf("l1");
        l1.setProperty("a", new PropertyValue().setValue("a"));
        l1.setProperty("b", new PropertyValue().setValue(3.14f));
        l1.setProperty("c", new PropertyValue().setValue(new java.util.Date(System.currentTimeMillis())));
        l1.setProperty("d", new PropertyValue().setValue(System.currentTimeMillis()));
//
        final Node cc1 = c1.createChildNode("cc1");
        cc1.setProperty("a", new PropertyValue().setValue("a"));
        cc1.setProperty("b", new PropertyValue().setValue(3.14f));
        cc1.setProperty("c", new PropertyValue().setValue(new java.util.Date(System.currentTimeMillis())));
        cc1.setProperty("d", new PropertyValue().setValue(System.currentTimeMillis()));
        cc1.setProperty("e", new PropertyValue().setValue('c'));
//
        final ObjectNode ret = root.toJSONObject();
        final String r1 = ret.toString();
        System.out.println(r1);

        final Node n = new Node(ret, 0);
        final String r2 = n.toJSONObject().toString();
        System.out.println(r2);

        System.out.println("---------------------------------------------------------------------------");
        System.out.println(r1.equals(r2));
    }

}