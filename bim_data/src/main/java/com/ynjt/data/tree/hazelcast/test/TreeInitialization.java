package com.ynjt.data.tree.hazelcast.test;

import com.hazelcast.core.IFunction;
import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.TreeProperty;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.io.Serializable;

public class TreeInitialization implements IFunction<TreeNode, TreeNode>, Serializable {

    private static int width = 10;
    private static int depth = 6;
    private static int cnt;
    private static final String[] ALPHA = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
            "x", "y", "z"
    };

    private String treeRootKey;

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        TreeInitialization.width = width;
    }

    public static int getDepth() {
        return depth;
    }

    public static void setDepth(int depth) {
        TreeInitialization.depth = depth;
    }

    public String getTreeRootKey() {
        return treeRootKey;
    }

    public TreeInitialization setTreeRootKey(final String treeRootKey) {
        this.treeRootKey = treeRootKey;
        return this;
    }

    @Override
    public TreeNode apply(TreeNode root) {
        if (root == null){
            root = new TreeNode();
            root.setId("Node[0]");
            root.setName("root");
            setProperties(root);
        }
        cnt = 0;
        populateTree(root);
        System.out.println("There are " + cnt + " TreeNode created");
//        System.out.println("after TreeInitialization.apply() root size = " + ObjectSizeCalculator.getObjectSize(root));
        return root;
    }

    
    private void populateTree(final TreeNode root){
        for(int wIndex = 0; wIndex < width; wIndex++){
            final TreeNode node = new TreeNode();
            cnt++;
            node.setId("Node[" + (root == null ? 0 : root.getDepth() * width  + wIndex) + "]");
//            node.setParent(root);
            setProperties(node);
            root.addChild(node);
            if(node.getDepth() < depth){
                populateTree(node);
            }
        }
    }

    private void setProperties(final TreeElement element){
        for(final String alpha : ALPHA){
            element.setProperty(alpha, new TreeProperty().setValue(alpha));
        }
    }

}