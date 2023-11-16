package io.dataplatform.process.core;

import io.dataplatform.common.pack.StreamPack;
import io.dataplatform.common.thread.SingletonProcess;

public class StreamPackCore extends SingletonProcess<StreamPack> {

    private static StreamPackCore instance;

    public static synchronized StreamPackCore getInstance(){
        if (instance == null) {
            instance = new StreamPackCore(100);
        }
        return instance;
    }

    private StreamPackCore(int queueSize) {
        super(StreamPackCore.class.getName(), true, queueSize);
    }

    @Override
    protected void process(StreamPack StreamPack) throws Exception {
        // TODO
    }


}
