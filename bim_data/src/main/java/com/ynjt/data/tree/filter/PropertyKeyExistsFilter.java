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
        if (element == null) {
            return null;
        }
        final TreeElement ret = element.getClass().equals(TreeNode.class) ? new TreeNode() : new TreeLeaf();
        boolean matchFound = false;

        if (element instanceof TreeNode) {
            for (final String childKey : ((TreeNode) element).getChildKeySet()) {
                final TreeElement targetElement = ((TreeNode) element).getChild(childKey);

                System.out.println("Key : " + childKey + " <--> " + (targetElement == null ? "null" : targetElement.getClass()));

                final TreeElement childElement = filter(targetElement);
                if (childElement != null) {
                    matchFound = true;
                    ((TreeNode) ret).addChild(childElement);
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