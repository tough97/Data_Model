package com.ynjs.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ynjs.data.excp.IllegalJSONFormatException;

public final class Leaf extends TreeElement{
    
    protected Leaf(final String id, final String name) {
        super(id, name);
    }

    protected Leaf(final ObjectNode node, final int depth) throws IllegalJSONFormatException {
        super(node, depth);
    }

}