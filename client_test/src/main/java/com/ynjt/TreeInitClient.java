package com.ynjt;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.server.AddChildNode;

public class TreeInitClient {


    public static void main(String[] args) {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("47.104.177.198:6900").setSmartRouting(true).setConnectionTimeout(5000);

        clientConfig.setProperty("hazelcast.client.heartbeat.interval", "2000");
        clientConfig.setProperty("hazelcast.client.heartbeat.timeout", "5000");
        clientConfig.setProperty("hazelcast.client.invocation.timeout.seconds", "10");

        final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        final IAtomicReference<TreeNode> rootReference = client.getAtomicReference("fun");

        //create root in tree
        System.out.println("Before alter");
        rootReference.alter(new AddChildNode());
        System.out.println("After alter");
    }
    

}
