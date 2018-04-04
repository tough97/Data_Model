package com.ynjt.server;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicReference;
import com.ynjt.data.tree.TreeNode;

public class HazelcastServer {
    public static void main(final String[] args) {
        final Config config = new Config();
        config.getNetworkConfig().setPort(6900).setPortAutoIncrement(false);
        config.getManagementCenterConfig().setEnabled(true).setUrl("http://localhost:8888/mancenter");

        final HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        final IAtomicReference<TreeNode> o = instance.getAtomicReference("fun");
        final TreeNode root = (TreeNode) new TreeNode().setName("root");
        o.set(root);
    }
}
