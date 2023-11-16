package io.dataplatform.common.thread;

import io.dataplatform.common.util.ExceptionUtil;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class RequestQueue<P> {

    private final LinkedList<P> queue;
    private int capacity;

    public RequestQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized P get(){
        while (queue.isEmpty()) {
            try{
                wait();
            } catch (InterruptedException e) {
                ExceptionUtil.ignore(e);
            }
        }
        return queue.removeFirst();
    }

    public synchronized void put(P p){
        while (queue.size() >= capacity) {
            P removed = queue.removeFirst();
            overflow(removed);
        }
        queue.addLast(p);
        notifyAll();
    }


    protected void overflow(P p){
        // do nothing
    };
}
