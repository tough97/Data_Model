package com.ynjt;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.hazelcast.core.ICompletableFuture;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.server.AddChildNode;

public class TreeInitClient {


    public static void main(String[] args) {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("47.104.177.198:6900").setSmartRouting(true).setConnectionTimeout(5000);

        clientConfig.setProperty("hazelcast.client.heartbeat.interval", "20000");
        clientConfig.setProperty("hazelcast.client.heartbeat.timeout", "99000");
        clientConfig.setProperty("hazelcast.client.invocation.timeout.seconds", "10");

        final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        final IAtomicReference<TreeNode> rootReference = client.getAtomicReference("fun_1");

        //create root in tree
        System.out.println("Before alter");
        final long start  = System.currentTimeMillis();
        final ICompletableFuture<Void> l = rootReference.alterAsync(new AddChildNode(10, 6));
        l.andThen(new ExecutionCallback<Void>() {

            @Override
            public void onResponse(final Void aVoid) {
                System.out.println("Tree planed with time " + (System.currentTimeMillis() - start));
            }

            @Override
            public void onFailure(final Throwable throwable) {
                System.out.println("Tree failed with time " + (System.currentTimeMillis() - start));
                throwable.printStackTrace();
            }
        });

        System.out.println("After alter");
    }
    

}
