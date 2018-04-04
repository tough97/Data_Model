package com.ynjt.server;

import com.hazelcast.core.IFunction;
import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.TreeNodeException;
import com.ynjt.data.tree.TreeProperty;

public class AddChildNode implements IFunction<TreeNode, TreeNode> {

    private static final int DEPTH = 10;
    private static final int WIDTH = 10;
    private static final String ID_PREFIX = "jiushiaidaoshenchucaiyoutaa,hahahaha";

    private static final String[] ALPHA = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
            "x", "y", "z"
    };

    public TreeNode apply(final TreeNode root) {
        try {
            populateChild(root);
        } catch (final TreeNodeException e) {
            e.printStackTrace();
        }
        return root;
    }

    private void addProperty(final TreeElement element) {
        for (final String alpha : ALPHA) {
            element.setProperty(alpha, new TreeProperty().setValue(alpha));
        }
    }

    private void populateChild(TreeNode root) throws TreeNodeException {
        if (root == null) {
            root = new TreeNode();
            addProperty(root.setName("root"));
            for (int index = 0; index < WIDTH; index++) {
                addProperty(root.addChild(ID_PREFIX + index, TreeNode.class));
            }
            populateChild(root);
        } else if (root.getChildrenSize() == 0) {
            if(root.getDepth() < (DEPTH - 1)){
                TreeNode child;
                for(int index = 0; index < WIDTH; index++){
                    child = root.addChild(ID_PREFIX + ((root.getDepth() + 1) * WIDTH + index), TreeNode.class);
                    addProperty(child);
                    if(child.getDepth() < DEPTH){
                        populateChild(child);
                    }
                }
            }
        }
    }

}