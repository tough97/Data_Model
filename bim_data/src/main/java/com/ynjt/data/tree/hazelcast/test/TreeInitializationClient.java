package com.ynjt.data.tree.hazelcast.test;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.*;
import com.ynjt.data.tree.TreeElement;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.hazelcast.TreeElementGetter;
import com.ynjt.data.tree.io.TreeJSONConverter;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.util.concurrent.ExecutionException;

public class TreeInitializationClient {

    public static final String ROOT_KEY = "rk";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final ClientConfig clientConfig = new ClientConfig();
//        clientConfig.addAddress("47.104.177.198:6900").setSmartRouting(true).setConnectionTimeout(5000);
//        clientConfig.addAddress("47.104.136.239:5701").setSmartRouting(true).setConnectionTimeout(5000);
        clientConfig.addAddress("localhost:6900").setSmartRouting(true).setConnectionTimeout(5000);
        clientConfig.setProperty("hazelcast.client.heartbeat.interval", "20000");
        clientConfig.setProperty("hazelcast.client.heartbeat.timeout", "99000");
        clientConfig.setProperty("hazelcast.client.invocation.timeout.seconds", "10");

        final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        final MultiMap<String, String> map = client.getMultiMap("hx");
        map.put("key", "value");
//        final IAtomicReference<TreeNode> treeNodeRefer =  client.getAtomicReference(ROOT_KEY);
//        TreeNode root = new TreeNode();
//        root.setName("Root");
//        treeNodeRefer.set(root);
//        System.out.println("Root planed");
//
//        //populate tree
//        TreeInitialization.setWidth(10);
//        TreeInitialization.setDepth(6);
//        long start = System.currentTimeMillis();
//        treeNodeRefer.alter(new TreeInitialization());
//
//        final ICompletableFuture<TreeNode> node = treeNodeRefer.applyAsync(new TreeInitialization());
//        node.andThen(new ExecutionCallback<TreeNode>() {
//            @Override
//            public void onResponse(final TreeNode response) {
//                long end = System.currentTimeMillis();
//                System.out.println("Populated remote tree with time " + (end - start));
//                client.shutdown();
//            }
//
//            @Override
//            public void onFailure(final Throwable t) {
//                t.printStackTrace();
//            }
//        });

//        root = treeNodeRefer.alterAndGet(new TreeInitialization());
//        long end = System.currentTimeMillis();
//        System.out.println("Tree populated with time " + (end - start));

        //get tree structure without properties
//        start = System.currentTimeMillis();
//        final ICompletableFuture<TreeNode> f = treeNodeRefer.alterAndGetAsync(new TreeElementGetter());
//        f.andThen(new ExecutionCallback<TreeNode>() {
//            private long start = System.currentTimeMillis();
//            @Override
//            public void onResponse(final TreeNode response) {
//                long end = System.currentTimeMillis();
//                System.out.println("Finished responding with time = " + (end - start));
//                System.out.println("ObjectSizeCalculator.getObjectSize(response) = " + ObjectSizeCalculator.getObjectSize(response));
//                try {
//                    System.out.println(new TreeJSONConverter().propertyToJson(f.get()));
//                } catch (final Exception e) {
//                    e.printStackTrace();
//                }
//                client.shutdown();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

}
