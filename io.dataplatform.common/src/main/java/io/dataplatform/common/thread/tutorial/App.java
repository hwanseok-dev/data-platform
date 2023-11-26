package io.dataplatform.common.thread.tutorial;

public class App {
    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new MyThread("thread0" + i).start();
        }
    }
}
