package com.ynjt.data.tree;

public final class TreeLeaf extends TreeElement{

    public TreeElement clone() {
        final TreeLeaf leaf = new TreeLeaf();
        cloneProperties(leaf);
        leaf.setPropertyListener(this.getPropertyListener());
        return leaf;
    }

}
