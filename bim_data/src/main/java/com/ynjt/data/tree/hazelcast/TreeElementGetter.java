package com.ynjt.data.tree.hazelcast;

import com.hazelcast.core.IFunction;
import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.filter.ProertiRemovalFilter;
import com.ynjt.data.tree.filter.TreeFilter;

import java.io.Serializable;

public class TreeElementGetter implements IFunction<TreeNode, TreeNode>, Serializable{

    private TreeFilter filter = new ProertiRemovalFilter();

    @Override
    public TreeNode apply(final TreeNode input) {
        return (TreeNode) filter.filter(input);
    }
}
