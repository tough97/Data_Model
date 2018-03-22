package com.ynjs.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ynjs.data.excp.IllegalJSONFormatException;
import com.ynjs.data.excp.TreeNodeConflictException;
import com.ynjs.data.excp.TreeNodeInstanciationException;
import com.ynjs.data.listener.NodeListener;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Node extends TreeElement{

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

    protected Node(final String id, final String name){
        super(id, name);
    }

    protected Node(final ObjectNode node, final int depth) throws IllegalJSONFormatException {
        super(node, depth);
        final JsonNode typeNode = node.get(TreeElement.NODE_TYPE);
        if(typeNode == null || typeNode.isNull()){
            throw new IllegalJSONFormatException("Node type information missing");
        }
        if(!typeNode.asText().equals(TreeElement.NODE)){
            throw new IllegalJSONFormatException("Node type = " + typeNode.asText());
        }
        final JsonNode childNodes = node.get(CHILDREN);
        if(!childNodes.isArray()){
            throw new IllegalJSONFormatException("Node children should be array");
        }
        
        for(final JsonNode child : childNodes){
            final JsonNode childTypeNode = child.get(TreeElement.NODE_TYPE);
            if(childTypeNode != null && !childTypeNode.isNull()){
                try {
                    final TreeElement childNode = childTypeNode.asText().equals(TreeElement.NODE) ?
                            new Node(TreeElement.toObjectNode(child), depth + 1) : childTypeNode.asText().equals(TreeElement.LEAF) ?
                            new Leaf(TreeElement.toObjectNode(child), depth + 1) : null;
                } catch (final IOException e) {
                    throw new IllegalJSONFormatException(e);
                }
            }
        }
    }

    protected Node addChild(final TreeElement child) throws TreeNodeConflictException {
        child.setParent(this);
        childrenMap.put(child.getId(), child);
        if(nodelistener != null){
            nodelistener.onChildAdded(this, child);
        }
        child.setDepth(this.getDepth() + 1);
        updated();
        return this;
    }

    public Node setNodeListener(final NodeListener listener){
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
            final Element ret = typeClass.getConstructor(String.class, String.class).newInstance(id, name);
            addChild(ret);
            return ret;
        } catch (final Exception e) {
            throw new TreeNodeInstanciationException(e);
        }
    }

    protected TreeElement removeChild(final String id) {
        return childrenMap.remove(id);
    }

    public TreeElement getChild(final String id){
        return childrenMap.get(id);
    }

    public Node createChildNode(final String name) throws TreeNodeInstanciationException {
        return createChild(name, Node.class);
    }

    public Leaf createChildLeaf(final String name) throws TreeNodeInstanciationException {
        return createChild(name, Leaf.class);
    }

    @Override
    protected ObjectNode toJSONObject(){
        final ObjectNode ret = super.toJSONObject();
        final ArrayNode childrenArray = ret.putArray(CHILDREN);
        for(final TreeElement child : childrenMap.values()){
            childrenArray.add(child.toJSONObject());
        }
        return ret;
    }

    public static void main(String[] args) {
        final Node root = new Node();
    }

}