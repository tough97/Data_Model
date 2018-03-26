package com.ynjt.data.tree.io;

import com.ynjt.data.tree.TreeElement;

public abstract class TreeLoader {

    public abstract TreeElement load() throws TreeLoadingException;

}
