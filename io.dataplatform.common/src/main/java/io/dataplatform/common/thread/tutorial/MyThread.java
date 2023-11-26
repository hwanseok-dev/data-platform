package io.dataplatform.common.thread.tutorial;

public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        for (int i = 0; i < 100_000; i++) {
            // increase에서는 synchronized 지정하지 않음
            MyObject.increaseStatic();
        }
        System.out.println(MyObject.getStaticNum());
        long after = System.currentTimeMillis();
        System.out.println(after - before + "ms");
    }
}
