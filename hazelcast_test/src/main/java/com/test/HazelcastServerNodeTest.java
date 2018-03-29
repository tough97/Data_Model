package com.test;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.ynjt.data.tree.*;
import com.ynjt.data.tree.filter.PropertyKeyExistsFilter;
import com.ynjt.data.tree.io.TreeJSONConverter;
import com.ynjt.data.tree.test.PerformanceTest;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

public class HazelcastServerNodeTest {

    private static final long MB = 1024 * 1024;

    private static int cnt = 0;

    public static void main(String[] args) throws TreeNodeException {
        final Config config = new Config();
        config.getNetworkConfig().setPort(5900).setPortAutoIncrement(false);
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        final IAtomicReference<TreeNode> tree = instance.getAtomicReference("Tree");
        tree.set((TreeNode) new TreeNode().setId("root").setName("root"));
        final TreeNode node = tree.get();
        node.setId("root");
        node.setName("root_name").setDepth(0);

        PerformanceTest.addChild(node);

        System.out.println("Tree Size = " + ObjectSizeCalculator.getObjectSize(node)/MB);
        System.out.println("There are " + cnt + " nodes in this tree");
        cnt --;
        long startTime = System.currentTimeMillis();
        final TreeNode target = (TreeNode) node.getDecendent("jiushiaidaoshenchucaiyoutaa,hahahaha" + cnt);
        long endTime = System.currentTimeMillis();
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
    

}
