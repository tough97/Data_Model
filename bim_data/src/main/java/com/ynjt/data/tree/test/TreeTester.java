package com.ynjt.data.tree.test;

import com.ynjt.data.tree.TreeLeaf;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.TreeNodeException;
import com.ynjt.data.tree.TreeProperty;
import com.ynjt.data.tree.filter.PropertyKeyExistsFilter;

import java.util.Date;

public class TreeTester {

    public static void main(String[] args) throws TreeNodeException {
        final TreeNode root = new TreeNode();
        final TreeNode child = root.addChild("child", "child1", TreeNode.class);
        child.addChild("child_leaf", "child_leaf", TreeLeaf.class)
                .setProperty("t", new TreeProperty().setValue("Target"));
        root.addChild("child_2", "child_2", TreeNode.class)
        .addChild("child_3", "child_3", TreeNode.class)
        .addChild("child_4", "child_4", TreeNode.class);
        root.addChild("child_5", "child_5", TreeNode.class)
        .setProperty("t", new TreeProperty().setValue(new Date(System.currentTimeMillis())))
        .setProperty("no", new TreeProperty().setValue(19811122));
        

        final TreeNode copy = (TreeNode) new PropertyKeyExistsFilter("t").filter(root);
        System.out.println("Here");
    }

}
