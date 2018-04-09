package com.ynjt.data.tree;

import com.ynjt.data.tree.listener.TreePropertyListener;
import com.ynjt.data.tree.util.IDGenerator;
import com.ynjt.data.tree.util.UUIDGenerator;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TreeElement implements Serializable{

    private static IDGenerator idGenerator = new UUIDGenerator();

    public static void setIdGenerator(final IDGenerator idGenerator) {
        TreeElement.idGenerator = idGenerator;
    }

    private static final long DEFAULT_MAX_ACCESS_TIME = 1000 * 60 * 10;
    private static final long DEFAULT_MAX_UPDATE_TIME = 1000 * 60 * 10;

    private ConcurrentHashMap<String, TreeProperty> properties =
            new ConcurrentHashMap<String, TreeProperty>();

    //functional properties
    private long lastAccessed;
    private long lastUpdated;
    private long maxAccessIdleTime = DEFAULT_MAX_ACCESS_TIME;
    private long maxUpdateIdleTime = DEFAULT_MAX_UPDATE_TIME;
    private TreePropertyListener propertyListener;

    //data properties
    private String id;
    private String name;
    protected int depth = 0;
    protected TreeNode parent = null;

    protected TreeElement() {
        this.id = idGenerator.generateUniuqeID();
        lastAccessed = System.currentTimeMillis();
        lastUpdated = System.currentTimeMillis();
    }

    public synchronized TreeElement accessed() {
        lastAccessed = System.currentTimeMillis();
        return this;
    }

    public synchronized TreeElement updated() {
        lastUpdated = System.currentTimeMillis();
        accessed();
        return this;
    }

    //data methods
    public TreeElement setProperty(final String key, final TreeProperty property) {
        final TreeProperty originalProperty = properties.put(key, property);
        if (propertyListener != null) {
            propertyListener.onPropertySet(key, originalProperty, property, this);
        }
        updated();
        return this;
    }

    public TreeProperty getProperty(final String key) {
        final TreeProperty property = properties.get(key);
        accessed();
        return property;
    }

    public TreeProperty removeProperty(final String key) {
        final TreeProperty property = properties.remove(key);
        if (propertyListener != null) {
            propertyListener.onPropertyDeleted(key, property, this);
        }
        updated();
        return property;
    }

    public String getName() {
        accessed();
        return name;
    }

    public synchronized TreeElement setName(final String name) {
        this.name = name;
        updated();
        return this;
    }

    public int getPropertiSize() {
        accessed();
        return properties.size();
    }

    //functional methods

    public Set<String> getPropertyKeySet() {
        accessed();
        return properties.keySet();
    }

    public synchronized TreeElement setParent(final TreeNode parent) {
        this.parent = parent;
        this.depth = parent == null ? 0 : parent.getDepth() + 1;
        return this;
    }

    public synchronized TreeElement setId(final String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    protected long getIdleTime() {
        return System.currentTimeMillis() - lastUpdated > lastAccessed ? lastUpdated : lastAccessed;
    }

    public TreeElement setPropertyListener(TreePropertyListener propertyListener) {
        this.propertyListener = propertyListener;
        return this;
    }

    public TreePropertyListener getPropertyListener() {
        return propertyListener;
    }

    protected void cloneProperties(final TreeElement target) {
        target.properties = new ConcurrentHashMap<String, TreeProperty>();
        target.properties.putAll(this.properties);
        target.id = this.id;
        target.name = this.name;
    }

    public abstract TreeElement clone();

    public long getMaxAccessIdleTime() {
        return maxAccessIdleTime;
    }

    public TreeElement setMaxAccessIdleTime(final long maxAccessIdleTime) {
        this.maxAccessIdleTime = maxAccessIdleTime;
        return this;
    }

    public long getMaxUpdateIdleTime() {
        return maxUpdateIdleTime;
    }

    public TreeElement setMaxUpdateIdleTime(final long maxUpdateIdleTime) {
        this.maxUpdateIdleTime = maxUpdateIdleTime;
        return this;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isLeaf() {
        return this.getClass().equals(TreeLeaf.class);
    }

    public boolean isNode() {
        return this.getClass().equals(TreeNode.class);
    }

    public TreeElement setDepth(final int depth){
        if(depth > 0){
            this.depth = depth;
        }
        return this;
    }
    
}