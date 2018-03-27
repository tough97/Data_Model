package com.ynjt.data.tree.filter;

import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeLeaf;
import com.ynjt.data.tree.TreeNode;

import java.util.HashSet;
import java.util.Set;

public class PropertyKeyExistsFilter implements TreeFilter {

    private Set<String> keySet = new HashSet<String>();

    public PropertyKeyExistsFilter(final String... keys) {
        for (final String key : keys) {
            keySet.add(key);
        }
    }

    //returns null if 
    public TreeElement filter(final TreeElement element) {
        final TreeElement ret = doFilter(element);
        ret.setDepth(0);
        return ret;
    }

    private TreeElement doFilter(final TreeElement element){
        if (element == null) {
            return null;
        }
        final TreeElement ret = element.getClass().equals(TreeNode.class) ? new TreeNode() : new TreeLeaf();
        boolean matchFound = false;

        if (element instanceof TreeNode) {
            for(final TreeElement childElement : ((TreeNode) element).getChildElements()){
                final TreeElement filteredElement = doFilter(childElement);
                if(filteredElement != null){
                    ((TreeNode)ret).addChild(filteredElement);
                    matchFound = true;
                }
            }
        }

        for (final String key : element.getPropertyKeySet()) {
            if (keySet.contains(key)) {
                matchFound = true;
                ret.setProperty(key, element.getProperty(key).clone());
            }
        }

        if(matchFound){
            ret.setId(element.getId()).setName(element.getName());
        }

        return matchFound ? ret : null;
    }

}