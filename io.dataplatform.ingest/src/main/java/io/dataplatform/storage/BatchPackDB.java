package io.dataplatform.storage;

import io.dataplatform.common.pack.BatchPack;
import io.dataplatform.common.thread.SingletonProcess;

public class BatchPackDB extends SingletonProcess<BatchPack> {

    private static BatchPackDB instance;

    public static synchronized BatchPackDB getInstance(){
        if (instance == null) {
            instance = new BatchPackDB(100);
        }
        return instance;
    }

    private BatchPackDB(int queueSize) {
        super(BatchPackDB.class.getName(), true, queueSize);
    }

    @Override
    protected void process(BatchPack batchPack) throws Exception {
        // TODO Upload to S3
        // 배치 데이터를 저장하기 위해 사용한다
        // 영구적으로 보관 기간을 설정하기 위해 사용한다
    }


}
