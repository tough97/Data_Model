package com.test;

import com.hazelcast.core.IFunction;
import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeLeaf;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.TreeProperty;

public class AddNodeFunction implements IFunction<TreeNode, TreeElement> {

    private TreeNode parent;
    private String name;
    private static final String[] ALPHA = {
            "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
    };
    private Class<? extends TreeElement> targetClass;

    public AddNodeFunction(TreeNode parent, String name, Class<? extends TreeElement> targetClass) {
        this.parent = parent;
        this.name = name;
        this.targetClass = targetClass;
    }

    public TreeElement apply(final TreeNode treeNode) {
        final TreeElement child = targetClass.equals(TreeNode.class) ? new TreeNode() : new TreeLeaf();
        addProperties(child);
        if(treeNode != null){
            treeNode.addChild(child);
        }
        return child;
    }

    private void addProperties(final TreeElement element){
        for(final String alpha : ALPHA){
            element.setProperty(alpha, new TreeProperty().setValue(alpha));
        }
    }

}
