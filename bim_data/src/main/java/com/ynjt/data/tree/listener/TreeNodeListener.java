package com.ynjt.data.tree.listener;

import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeNode;

public interface TreeNodeListener {

    void onChildAdded(final TreeElement childElement, final TreeNode node);
    void onChildRemoved(final TreeElement childElement, final TreeNode node);

}