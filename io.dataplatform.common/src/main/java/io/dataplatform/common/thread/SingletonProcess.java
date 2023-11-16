package io.dataplatform.common.thread;

public abstract class SingletonProcess<P> extends Thread {

    private String name;
    private boolean daemon;
    private int queueSize;
    private boolean run = true;

    private RequestQueue<P> queue;
    protected SingletonProcess(String name, boolean daemon, int queueSize) {
        this.name = name;
        this.daemon = daemon;
        this.queue = new RequestQueue<>(queueSize);

        setName(name);
        setDaemon(daemon);
        start();
    }

    @Override
    public void run() {
        while (run){
            P p = queue.get();
            try{
                process(p);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void add(P p) {
        queue.put(p);
    }

    abstract protected void process(P p) throws Exception;
}
