package com.test;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.ynjt.MemCalulator;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.test.PerformanceTest;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.io.IOException;


public class HazelCastTreeGetClient {

    public static void main(String[] args) {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("47.104.177.198:6900").setSmartRouting(true).setConnectionTimeout(5000);
        clientConfig.setProperty("hazelcast.client.heartbeat.interval", "20000");
        clientConfig.setProperty("hazelcast.client.heartbeat.timeout", "99000");
        clientConfig.setProperty("hazelcast.client.invocation.timeout.seconds", "10");

        final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        final IAtomicReference<TreeNode> rootRef = client.getAtomicReference("fun");
        System.out.println("start");
        long start = System.currentTimeMillis();
        final TreeNode root = rootRef.get();
        System.out.println("Finished time used = " + (System.currentTimeMillis() - start));
        System.out.println("Tree size " + ObjectSizeCalculator.getObjectSize(root) / MemCalulator.MB + " MB");

        System.out.println("Start writing file");
        start = System.currentTimeMillis();
        try {
            PerformanceTest.writeObjectToFile(root);
            System.out.println("Writing finished with time " + (System.currentTimeMillis() - start));
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }
    

}
