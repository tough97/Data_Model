package com.ynjt.data.tree;

import com.ynjt.data.tree.util.IDGenerator;
import com.ynjt.data.tree.util.UUIDGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TreeNode extends TreeElement{

    private static IDGenerator idGenerator = new UUIDGenerator();

    public static void setIdGenerator(final IDGenerator idGenerator) {
        TreeNode.idGenerator = idGenerator;
    }
    
    private ConcurrentHashMap<String, TreeElement> childrenElements =
            new ConcurrentHashMap<String, TreeElement>();

    public<Target extends TreeElement> Target addChild(final String id, final String name, final Class<? extends Target> targetClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor<Target> constructor = (Constructor<Target>) targetClass.getDeclaredConstructor();
        final TreeElement child = constructor.newInstance();
        childrenElements.put(child.setName(name).setId(id).getId(), child.setParent(this));
        return (Target) child;
    }

    public TreeNode addChild(final TreeElement element){
        element.setParent(this);
        childrenElements.put(element.getId(), element);
        return this;
    }

    public TreeElement getChild(final String key){
        return childrenElements.get(key);
    }

    public Set<String> getChildKeySet(){
        return childrenElements.keySet();
    }

    public int getChildrenSize(){
        return childrenElements.size();
    }

    public TreeElement clone() {
        final TreeNode node = new TreeNode();
        for(final TreeElement element : childrenElements.values()){
            final TreeElement clonedElement = element.clone();
            node.childrenElements.put(element.getId(), element.clone());
        }
        cloneProperties(node);
        node.setPropertyListener(this.getPropertyListener());
        return node;
    }
}