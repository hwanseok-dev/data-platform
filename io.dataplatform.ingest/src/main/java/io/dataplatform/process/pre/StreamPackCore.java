package io.dataplatform.process.pre;

import io.dataplatform.common.pack.StreamPack;
import io.dataplatform.common.thread.SingletonProcess;
import io.dataplatform.storage.StreamPackDB;

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
    protected void process(StreamPack p) throws Exception {
        // 캐시에 있는 데이터를 사용해서 처리를 할 수 있다
        // 대용량 데이터를

        // 수집 계층을 통과한 이후 안정적으로 저장될 수 있어야 한다
        // 영속성을 가지는 queue 형태의 분산 로그 시스템
        // TODO overflow가 되었을 때의 처리를 추가해야한다
        // TODO overflow에 대해서도 meta 계층에 정보가 추가되어야 한다
        StreamPackDB.getInstance().add(p);
    }


}
