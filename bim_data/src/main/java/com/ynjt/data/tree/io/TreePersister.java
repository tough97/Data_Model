package com.ynjt.data.tree.io;

import com.ynjt.data.tree.TreeElement;

public abstract class TreePersister {

    public abstract void persist(final TreeElement element) throws TreePersistanceException;

}