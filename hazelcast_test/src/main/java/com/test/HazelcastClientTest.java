package com.test;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.SocketOptions;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.ynjt.data.tree.TreeNode;
import com.ynjt.data.tree.TreeNodeException;
import com.ynjt.data.tree.io.TreeJSONConverter;
import com.ynjt.data.tree.test.PerformanceTest;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;


public class HazelcastClientTest {

    private static final long MB_SIZE = 1024 * 1024;

    private static long getMBSize(final long sizeInByte){
        return sizeInByte/MB_SIZE;
    }

    public static void main(final String[] args) throws TreeNodeException {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("localhost:5900", "47.104.177.198:5900").setSmartRouting(true).setConnectionTimeout(5000);

        final SocketOptions socketOptions = clientConfig.getNetworkConfig().getSocketOptions();
        //client set up
        socketOptions.setBufferSize(32);
        socketOptions.setKeepAlive(true);
        socketOptions.setTcpNoDelay(true);
        socketOptions.setReuseAddress(true);
        socketOptions.setLingerSeconds(3);
        //management center set up
        //config.getManagementCenterConfig().setEnabled(true).setUrl("http://localhost:8888/mancenter");


        clientConfig.setProperty("hazelcast.client.event.thread.count", "20");
        clientConfig.setProperty("hazelcast.client.heartbeat.interval", "10000");
        clientConfig.setProperty("hazelcast.client.heartbeat.timeout", "60000");
        clientConfig.setProperty("hazelcast.client.statistics.enabled", "true");
        

        final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        final TreeNode rootNode = (TreeNode) new TreeNode().setName("root");
        System.out.println();
        long start = System.currentTimeMillis();
        System.out.println("root creation started");
        PerformanceTest.addChild(rootNode);
        long end = System.currentTimeMillis();
        System.out.println("root creation finished, Time used = " + (end - start) + " tree size = " + (ObjectSizeCalculator.getObjectSize(rootNode)/MB_SIZE));
        final HazelcastInstance myClient = HazelcastClient.newHazelcastClient(clientConfig);
        final IAtomicReference<TreeNode> nodeRoot = myClient.getAtomicReference("mt");
//        start = System.currentTimeMillis();
//        System.out.println("root start uploading");
//
//        final ICompletableFuture task = nodeRoot.setAsync(rootNode);
//        task.andThen(new ExecutionCallback() {
//            public void onResponse(final Object o) {
//                System.out.println("done");
//            }
//
//            public void onFailure(Throwable throwable) {
//                System.out.println(throwable.getMessage());
//            }
//        });
//        end = System.currentTimeMillis();

        System.out.println("Start uploading");
        start = System.currentTimeMillis();
        nodeRoot.set(rootNode);
        end = System.currentTimeMillis();

        System.out.println("root start uploading, Time used = " + (end - start));

        client.shutdown();
        myClient.shutdown();
        
        checkLatest();
    }

    private static void checkLatest(){
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("47.104.177.198:5900", "127.0.0.1:5900")
                .setSmartRouting(true).setConnectionTimeout(5000);

        final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        final IAtomicReference<TreeNode> tee = client.getAtomicReference("mt");
        final TreeNode root = tee.get();
        System.out.println(new TreeJSONConverter().propertyToJson(root));
    }

}
