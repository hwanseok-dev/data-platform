package io.dataplatform.storage;

import io.dataplatform.common.pack.StreamPack;
import io.dataplatform.common.thread.SingletonProcess;

public class StreamPackDB extends SingletonProcess<StreamPack> {

    private static StreamPackDB instance;

    public static synchronized StreamPackDB getInstance(){
        if (instance == null) {
            instance = new StreamPackDB(100);
        }
        return instance;
    }

    private StreamPackDB(int queueSize) {
        super(StreamPackDB.class.getName(), true, queueSize);
    }

    @Override
    protected void process(StreamPack StreamPack) throws Exception {
        // TODO Save to File DB
        // 수집 서버 내부에 저장하는 고속 스토리지의 특징을 가진다
        // DISK 공간이 한정되어 있기 때문에 데이터 만료 정책을 가져야 한다
    }


}
