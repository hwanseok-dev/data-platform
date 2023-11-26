package io.dataplatform.common.thread.tutorial;

public class MyObject {

    private static MyObject instance;

    public static synchronized MyObject getInstance(){
        if (instance == null) {
            instance = new MyObject();
        }
        return instance;
    }

    private int num = 0;
    private static int staticNum = 0;

    public void increase(){
        num++;
    }

    public static synchronized void increaseStatic(){
        staticNum++;
    }

    public int getNum(){
        return num;
    }

    public static int getStaticNum(){
        return staticNum;
    }
}
