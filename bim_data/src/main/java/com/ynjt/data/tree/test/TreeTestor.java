package com.ynjt.data.tree.test;

import com.ynjt.data.tree.TreeLeaf;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.TreeProperty;
import com.ynjt.data.tree.filter.PropertyKeyExistsFilter;

import java.lang.reflect.InvocationTargetException;

public class TreeTestor {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TreeNode root = new TreeNode();
        final TreeNode child = root.addChild("child", "child1", TreeNode.class);
        child.addChild("child_leaf", "child_leaf", TreeLeaf.class).setProperty("t", new TreeProperty().setValue("Target"));
        root.addChild("child_2", "child_2", TreeNode.class);

        final TreeNode copy = (TreeNode) new PropertyKeyExistsFilter("t").filter(root);
        System.out.println("Here");
    }

}
