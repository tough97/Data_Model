package com.test;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.ynjt.data.tree.TreeNode;


public class HazelcastClientTest {

    public static void main(final String[] args) {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("47.104.177.198:5900").setSmartRouting(true).setConnectionTimeout(5000);
        final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        final IAtomicReference<TreeNode> node = client.getAtomicReference("Tree");
        System.out.println(node.get().getName());
    }

}
