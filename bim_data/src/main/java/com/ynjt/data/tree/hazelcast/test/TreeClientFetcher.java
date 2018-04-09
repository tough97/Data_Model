package com.ynjt.data.tree.hazelcast.test;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.hazelcast.TreeElementGetter;

public class TreeClientFetcher {

    public static void main(String[] args) {
        final ClientConfig clientConfig = new ClientConfig();
//        clientConfig.addAddress("47.104.177.198:6900").setSmartRouting(true).setConnectionTimeout(5000);
//        clientConfig.addAddress("47.104.136.239:5701").setSmartRouting(true).setConnectionTimeout(5000);
        clientConfig.addAddress("localhost:6900").setSmartRouting(true).setConnectionTimeout(5000);
        clientConfig.setProperty("hazelcast.client.heartbeat.interval", "200000");
        clientConfig.setProperty("hazelcast.client.heartbeat.timeout", "900000");
        clientConfig.setProperty("hazelcast.client.invocation.timeout.seconds", "10");
        final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        final IAtomicReference<TreeNode> treeNodeRefer = client.getAtomicReference(TreeInitializationClient.ROOT_KEY);
        long start = System.currentTimeMillis();
        final TreeNode root = treeNodeRefer.alterAndGet(new TreeElementGetter());
        long end = System.currentTimeMillis();
        System.out.println("Tree populated with time " + (end - start));
    }
}
