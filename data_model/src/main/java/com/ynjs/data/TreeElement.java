package com.ynjs.data;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ynjs.data.excp.IllegalJSONFormatException;
import com.ynjs.data.excp.TreeNodeConflictException;
import com.ynjs.data.listener.PropertyListener;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TreeElement is abstracted class for Leaf and Node classes
 * when implement the sub-class, pay attention to concurrency
 * since tree should be thread safe
 */

public abstract class TreeElement {

    protected static final String ID = "id";
    protected static final String NAME = "name";
    protected static final String DEPTH = "depth";
    protected static final String PROPERTIES = "props";
    protected static final String PROPERTY_KEY = "p_k";
    //used to indicate if this node is leaf or not
    protected static final String NODE_TYPE = "n_t";
    protected static final String LEAF = "leaf";
    protected static final String NODE = "node";

    protected static ObjectNode toObjectNode(final JsonNode node) {
        return node.deepCopy();
    }

    private String id;
    private String name;
    private int depth;

    private long lastUpdated;
    private long lastRead;
    private PropertyListener propertyListener = null;
    private Node parent = null;
    private ConcurrentHashMap<String, PropertyValue> properties = new ConcurrentHashMap<>();


    protected TreeElement(final ObjectNode node, final int depth) throws IllegalJSONFormatException {
        final JsonNode idNode = node.get(ID);
        final JsonNode nameNode = node.get(NAME);
        final JsonNode propsNode = node.get(PROPERTIES);


        if (idNode == null || nameNode == null) {
            throw new IllegalJSONFormatException("ID, and name should present");
        }
        this.id = idNode.asText();
        this.name = nameNode.asText();
        this.depth = depth;

        if (propsNode != null) {
            for (final JsonNode prop : propsNode) {
                final JsonNode keyNode = prop.get(PROPERTY_KEY);
                if (keyNode != null && !keyNode.isNull()) {
                    properties.put(keyNode.asText(), new PropertyValue(toObjectNode(prop)));
                }
            }
        }
    }

    protected TreeElement(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    protected TreeElement setDepth(final int depth) {
        this.depth = depth;
        return this;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isRoot() {
        return parent == null;
    }

    protected void updated() {
        lastUpdated = System.currentTimeMillis();
        accessed();
        if (parent != null) {
            parent.updated();
        }
    }

    protected void accessed() {
        lastRead = System.currentTimeMillis();
        if (parent != null) {
            parent.accessed();
        }
    }

    protected TreeElement setParent(final Node node) throws TreeNodeConflictException {
        if (node == null) {
            throw new TreeNodeConflictException("Parent node can not be null");
        }
        synchronized (this) {
            if (parent != null) {
                throw new TreeNodeConflictException();
            } else {
                this.parent = node;
            }
        }
        lastUpdated = System.currentTimeMillis();
        updated();
        return this;
    }

    public TreeElement setName(final String name) {
        this.name = name;
        updated();
        return this;
    }

    public String getId() {
        accessed();
        return id;
    }

    public String getName() {
        accessed();
        return name;
    }

    public TreeElement setProperty(final String key, final PropertyValue value) {
        final PropertyValue oldValue = properties.put(key, value);
        if (propertyListener != null) {
            propertyListener.onPropertySet(this, key, oldValue, value);
        }
        updated();
        return this;
    }

    public PropertyValue removeProperty(final String key) {
        final PropertyValue value = properties.remove(key);
        if (value != null && propertyListener != null) {
            propertyListener.onPropertyRemoved(this, key, value);
        }
        updated();
        return value;
    }

    public PropertyValue getProperty(final String key) {
        return properties.get(key);
    }

    public int getPropertySize() {
        return properties.size();
    }

    public boolean isNode() {
        return this.getClass().equals(Node.class);
    }

    public boolean isLeaf() {
        return this.getClass().equals(Leaf.class);
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public long getLastRead() {
        return lastRead;
    }

    protected ObjectNode toJSONObject() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode ret = mapper.createObjectNode();
        ret.put(ID, id).put(NAME, name).put(DEPTH, depth).put(NODE_TYPE, isLeaf() ? LEAF : NODE);
        if (!properties.isEmpty()) {
            final ArrayNode propArray = ret.putArray(PROPERTIES);
            for (final String key : properties.keySet()) {
                final ObjectNode node = mapper.createObjectNode().put(PROPERTY_KEY, key);
                properties.get(key).putValue(node);
                propArray.add(node);
            }
        }
        return ret;
    }

}