package com.ynjs.test;


import java.lang.Runtime;

import static java.lang.Thread.sleep;

public class OSHITest {

    private static final long MB = 1024 * 1024;

    private static long byteToMB(final long b){
        return b / MB;
    }

    public static void main(String[] args) {
        final Runtime runtime = Runtime.getRuntime();
        System.out.println("JVM max memory : " + byteToMB(runtime.maxMemory()));
        System.out.println("JVM total memory : " + byteToMB(runtime.totalMemory()));
        System.out.println("JVM available memory : " + byteToMB(runtime.freeMemory()));
        System.out.println("JVM processes : " + runtime.availableProcessors());

        final Thread t = new Thread(new Runnable() {
            private int cnt = 10000;
            public void run() {
                int c = 0;
                while(c < cnt){
                    try {
                        sleep(1000);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                    c++;
                }
            }
        });
        t.start();
    }

}
