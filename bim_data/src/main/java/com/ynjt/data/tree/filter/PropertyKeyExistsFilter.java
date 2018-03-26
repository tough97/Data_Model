package com.ynjt.data.tree.filter;

import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeLeaf;
import com.ynjt.data.tree.TreeNode;

import java.util.HashSet;
import java.util.Set;

public class PropertyKeyExistsFilter implements TreeFilter {

    private Set<String> keySet = new HashSet<String>();

    public PropertyKeyExistsFilter(final String... keys) {
        for(final String key : keys){
            keySet.add(key);
        }
    }

    //returns null if 
    public TreeElement filter(final TreeElement element) {
        if(element == null){
            return null;
        }
        final TreeElement ret = element.getClass().equals(TreeNode.class) ? new TreeNode() : new TreeLeaf();
        boolean matchFound = false;
        
        if(ret instanceof TreeNode){
            for(final String childKey : ((TreeNode) ret).getChildKeySet()){
                final TreeElement childElement = filter(((TreeNode) ret).getChild(childKey));
                if(childElement != null){
                    matchFound = true;
                    ((TreeNode) ret).addChild(element);
                }
            }
        }
        
        for(final String key : element.propertiKeySet()){
            if(keySet.contains(key)){
                matchFound = true;
                ret.setProperty(key, element.getProperty(key).clone());
            }
        }
        return matchFound ? ret : null;
    }



}