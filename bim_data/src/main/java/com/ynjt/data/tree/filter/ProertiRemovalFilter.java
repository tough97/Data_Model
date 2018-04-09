package com.ynjt.data.tree.filter;

import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeLeaf;
import com.ynjt.data.tree.TreeNode;

public final class ProertiRemovalFilter implements TreeFilter{
    
    @Override
    public TreeElement filter(final TreeElement element) {
        if(element == null){
            return null;
        } else {
            return filterProperties(element);
        }
    }

    private TreeElement filterProperties(final TreeElement element){
        final TreeElement ret = element instanceof TreeLeaf ? new TreeLeaf() : new TreeNode();
        ret.setName(element.getName()).setDepth(element.getDepth()).setId(element.getId());
        if(ret instanceof TreeNode){
            final TreeNode node = (TreeNode) ret;
            for(final TreeElement child : node.getChildElements()){
                node.addChild(filter(child));
            }
        }
        return ret;
    }

}
