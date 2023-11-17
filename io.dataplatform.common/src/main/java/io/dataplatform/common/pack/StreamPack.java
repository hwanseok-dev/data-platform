package io.dataplatform.common.pack;

import java.util.HashMap;
import java.util.Map;

public class StreamPack extends AbstractPack {

    private final Map<String, Object> fields = new HashMap<>(); // 소스 시스템에서 발생한 데이터

    public void putField(String key, Object value){
        fields.put(key, value);
    }

    public Object getField(String key){
        return fields.get(key);
    }

    // TODO Object 타입의 값을 어떻게 다루어야할까? -> CastUtil
}
