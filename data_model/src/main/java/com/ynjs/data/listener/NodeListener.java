package com.ynjs.data.listener;


import com.ynjs.data.Node;
import com.ynjs.data.TreeElement;

public interface NodeListener extends PropertyListener {

    void onChildAdded(final Node node, final TreeElement child);
    void onChildRemoved(final Node node, final TreeElement child);

}