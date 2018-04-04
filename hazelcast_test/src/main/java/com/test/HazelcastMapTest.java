package com.test;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.ConcurrentMap;

public class HazelcastMapTest {

    public static void main(String[] args) {
        final Config config = new Config();
        config.getNetworkConfig().setPort(5900).setPortAutoIncrement(false);
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

        final ConcurrentMap<String, String> map = instance.getMap("my-map");
        for(int i = 0; i < Integer.MAX_VALUE; i++){
            map.putIfAbsent(String.valueOf(i), String.valueOf(i));
        }
    }

}
