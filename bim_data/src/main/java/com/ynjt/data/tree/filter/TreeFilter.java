package com.ynjt.data.tree.filter;

import com.ynjt.data.tree.TreeElement;

import java.io.Serializable;

public interface TreeFilter extends Serializable{
    //filter a tree element clone corresponding properties
    TreeElement filter(final TreeElement element);
}