package com.ynjt.data.tree;

import com.ynjt.data.tree.listener.TreeNodeListener;
import com.ynjt.data.tree.util.IDGenerator;
import com.ynjt.data.tree.util.UUIDGenerator;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TreeNode extends TreeElement {
    
    private TreeNodeListener nodeListener;

    private ConcurrentHashMap<String, TreeElement> childrenElements =
            new ConcurrentHashMap<String, TreeElement>();

    public <Target extends TreeElement> Target addChild(final String name, final Class<? extends Target> targetClass)
            throws TreeNodeException {
        try {
            final Constructor<Target> constructor = (Constructor<Target>) targetClass.getDeclaredConstructor();
            final TreeElement child = constructor.newInstance();
            childrenElements.put(child.setName(name).getId(), child.setParent(this));
            if (nodeListener != null) {
                nodeListener.onChildAdded(child, this);
            }
            updated();
            return (Target) child;
        } catch (final Exception ex) {
            throw new TreeNodeException(ex);
        }
    }

    public TreeNode addChild(final TreeElement element) {
        element.setParent(this);
        childrenElements.put(element.getId(), element);
        if (nodeListener != null) {
            nodeListener.onChildAdded(element, this);
        }
        updated();
        return this;
    }

    public TreeElement removeChild(final String key) {
        final TreeElement element = childrenElements.remove(key);
        if (element != null) {
            if (nodeListener != null) {
                nodeListener.onChildRemoved(element, this);
            }
            element.setParent(null);
            element.setDepth(0);
            updated();
        } else {
            accessed();
        }
        return element;
    }
    
    /**
     * This method returns the decendent of this node no matter the depth
     * if no decendent if found, null will be returned
     *
     * @param key
     * @return
     */
    public TreeElement getDecendent(final String key) {
        for (final TreeElement child : childrenElements.values()) {
            if (child.getId().equals(key)) {
                return child;
            } else if (child instanceof TreeNode) {
                final TreeElement target = ((TreeNode) child).getDecendent(key);
                if (target != null) {
                    return target;
                }
            }
        }
        return null;
    }

    public TreeElement getChild(final String key) {
        accessed();
        return childrenElements.get(key);
    }

    public Set<String> getChildKeySet() {
        return childrenElements.keySet();
    }

    public Collection<TreeElement> getChildElements() {
        return childrenElements.values();
    }

    public int getChildrenSize() {
        return childrenElements.size();
    }

    @Override
    public TreeNode setDepth(final int depth) {
        super.setDepth(depth);
        for (final TreeElement child : childrenElements.values()) {
            child.setDepth(depth + 1);
        }
        return this;
    }

    public TreeElement clone() {
        final TreeNode node = new TreeNode();
        for (final TreeElement element : childrenElements.values()) {
            node.childrenElements.put(element.getId(), element.clone());
        }
        cloneProperties(node);
        node.setPropertyListener(this.getPropertyListener());
        return node;
    }

    public TreeNode setNodeListener(final TreeNodeListener nodeListener) {
        this.nodeListener = nodeListener;
        return this;
    }
}