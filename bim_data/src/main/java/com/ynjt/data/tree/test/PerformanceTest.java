package com.ynjt.data.tree.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.TreeNodeException;
import com.ynjt.data.tree.TreeProperty;
import com.ynjt.data.tree.filter.PropertyKeyExistsFilter;
import com.ynjt.data.tree.io.TreeJSONConverter;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.io.*;


public class PerformanceTest {

    private static final int WIDTH = 10;
    private static final int DEPTH = 6;
    private static final String ID_PREFIX = "jiushiaidaoshenchucaiyoutaa,hahahaha";

    private static final long MB = 1024 * 1024;

    private static int cnt = 0;

    public static void main(String[] args) throws TreeNodeException {
        cnt = 0;

        
        final TreeNode node = new TreeNode();
        node.setId("root");
        node.setName("root_name").setDepth(0);

        long startTime = System.currentTimeMillis();
        addChild(node);
        long endTime = System.currentTimeMillis();
        System.out.println("Time used to construct tree = " + (endTime - startTime));

        try {
            writeObjectToFile(node);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        System.out.println("Tree Size = " + ObjectSizeCalculator.getObjectSize(node)/MB);
        System.out.println("There are " + cnt + " nodes in this tree");
        cnt --;
        startTime = System.currentTimeMillis();
        final TreeNode target = (TreeNode) node.getDecendent("jiushiaidaoshenchucaiyoutaa,hahahaha" + cnt);
        endTime = System.currentTimeMillis();
        System.out.println("Time used " +(endTime - startTime) + (target == null ? " not found" : " " + target.toString()));
        System.out.println("target.getDepth() = "+target.getDepth());

        startTime = System.currentTimeMillis();
        final TreeElement element = new PropertyKeyExistsFilter("wo").filter(node);
        endTime = System.currentTimeMillis();
        System.out.println(element.toString());
        System.out.println(element.getDepth());
        System.out.println("Filter time used = " + (endTime - startTime));

        System.out.println("--------------------------------------------");

        System.out.println(new TreeJSONConverter().propertyToJson(element).toString());
    }

    public static void writeObjectToFile(final Serializable obj) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(new File("/home/gang_liu/Desktop/test.json"));
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        final long start = System.currentTimeMillis();
        objectOutputStream.writeObject(obj);
        final long end = System.currentTimeMillis();
        System.out.println("Time used to write object to file " + (end - start));
        objectOutputStream.close();
    }

    private static final String[] alpha = {
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
    };

    public static TreeNode[] addChild(final TreeNode root) throws TreeNodeException {
        final TreeNode[] ret = new TreeNode[WIDTH];
        for(int i = 0; i < WIDTH; i++){
            ret[i] = root.addChild("name", TreeNode.class);
            ret[i].setId(ID_PREFIX + (cnt++));
            for(final String k : alpha){
                ret[i].setProperty(k, new TreeProperty().setValue(k));
            }
            if(cnt == 1000000){
                System.out.println("Node set wo property = " + ret[i].toString());
                ret[i].setProperty("wo", new TreeProperty().setValue("wo"));
            }
            if(ret[i].getDepth() < DEPTH){
                addChild(ret[i]);
            }
        }
        return ret;
    }

}
