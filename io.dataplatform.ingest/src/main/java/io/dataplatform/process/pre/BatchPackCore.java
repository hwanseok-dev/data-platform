package io.dataplatform.process.pre;

import io.dataplatform.common.pack.BatchPack;
import io.dataplatform.common.thread.SingletonProcess;
import io.dataplatform.storage.BatchPackDB;

public class BatchPackCore extends SingletonProcess<BatchPack> {

    private static BatchPackCore instance;

    public static synchronized BatchPackCore getInstance(){
        if (instance == null) {
            instance = new BatchPackCore(100);
        }
        return instance;
    }

    private BatchPackCore(int queueSize) {
        super(BatchPackCore.class.getName(), true, queueSize);
    }

    @Override
    protected void process(BatchPack p) throws Exception {
        BatchPackDB.getInstance().add(p);
    }


}
