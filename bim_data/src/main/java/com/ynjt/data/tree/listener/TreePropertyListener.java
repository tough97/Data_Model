package com.ynjt.data.tree.listener;

import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeProperty;

public interface TreePropertyListener {

    void onPropertySet(final String key, final TreeProperty originalProperty, final TreeProperty newProperty, final TreeElement treeElement);
    void onPropertyDeleted(final String key, final TreeProperty property, final TreeElement treeElement);
    
}