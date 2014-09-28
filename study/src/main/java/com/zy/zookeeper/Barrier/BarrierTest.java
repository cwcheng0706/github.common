package com.zy.zookeeper.Barrier;

import java.util.Random;

public class BarrierTest {

    public static void main(String args[]) throws Exception {
        for (int i = 0; i < 300; i++) {
            Process p = new Process("Thread-" + i, new Barrier("/app1/barrier", 300));
            p.start();
        }
    }
}

class Process extends Thread {

    private String  name;
    private Barrier barrier;

    public Process(String name, Barrier barrier){
        this.name = name;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            barrier.enter(name);
            Thread.sleep(1000 + new Random().nextInt(2000)); 
//            Thread.sleep(14000);
            barrier.leave(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
