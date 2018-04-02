package com.test;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import com.hazelcast.logging.LogEvent;
import com.hazelcast.logging.LogListener;
import com.hazelcast.logging.LoggingService;
import com.ynjt.data.tree.*;

import java.util.Date;
import java.util.logging.Level;

public class HazelcastServerNodeTest {

    public static void main(String[] args) throws TreeNodeException {
        final Config config = new Config();
        config.getNetworkConfig().setPort(5900).setPortAutoIncrement(false);
        config.getManagementCenterConfig().setEnabled(true).setUrl("http://localhost:8888/mancenter");

        final HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        final LoggingService service = instance.getLoggingService();
        service.addLogListener(Level.INFO, new LogListener() {
            public void log(LogEvent logEvent) {
            }
        });
        final IAtomicReference<TreeNode> tree = instance.getAtomicReference("Tree");
        tree.set(createTree());
    }
    
    private static TreeNode createTree() throws TreeNodeException {
        final TreeNode root = new TreeNode();
        final TreeNode child = root.addChild("child1", TreeNode.class);
        child.addChild("child_leaf", TreeLeaf.class)
                .setProperty("t", new TreeProperty().setValue("Target"));
        root.addChild("child_2", TreeNode.class)
                .addChild("child_3", TreeNode.class)
                .addChild("child_4", TreeNode.class).setProperty("k", new TreeProperty().setValue("p"));
        root.addChild("child_5", TreeNode.class)
                .setProperty("t", new TreeProperty().setValue(new Date(System.currentTimeMillis())))
                .setProperty("no", new TreeProperty().setValue(19811122));
        return root;
    }
}
