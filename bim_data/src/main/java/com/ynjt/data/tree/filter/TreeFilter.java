package com.ynjt.data.tree.filter;

import com.ynjt.data.tree.TreeElement;

public interface TreeFilter {
    //filter a tree element clone corresponding properties
    TreeElement filter(final TreeElement element);
}