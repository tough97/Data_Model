package com.ynjt.data.tree.hazelcast;

import com.hazelcast.core.IFunction;
import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeLeaf;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.TreeProperty;
import com.ynjt.data.tree.util.IDGenerator;
import com.ynjt.data.tree.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AddChildRemoteImpl implements IFunction<TreeNode, TreeElement>, Serializable {

    private static final Logger logger = Logger.getLogger(AddChildRemoteImpl.class);
    private static IDGenerator idGenerator = new UUIDGenerator();

    private String name;
    private Map<String, TreeProperty> propertyMap = new HashMap<>();
    private Class<? extends TreeElement> type = TreeNode.class;

    public String getName() {
        return name;
    }

    public AddChildRemoteImpl setName(final String name) {
        this.name = name;
        return this;
    }

    public TreeProperty getProperties(final String key) {
        return propertyMap.get(key);
    }

    public AddChildRemoteImpl addProperties(final String key, final TreeProperty property){
        propertyMap.put(key, property);
        return this;
    }

    public Class<? extends TreeElement> getType() {
        return type;
    }

    public AddChildRemoteImpl setType(final Class<? extends TreeElement> type) {
        this.type = type;
        return this;
    }

    @Override
    public TreeElement apply(final TreeNode parent) {
        final TreeElement child = type.equals(TreeNode.class) ? new TreeNode() : new TreeLeaf();
        child.setName(name).setParent(parent);
        for(final String key : propertyMap.keySet()){
            child.setProperty(key, propertyMap.get(key));
        }
        return child;
    }
}
