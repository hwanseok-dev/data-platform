package io.dataplatform.common.pack;

import io.dataplatform.common.util.DateUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 정의
 * 1. StreamPack : 한 번에 하나의 이벤트 데이터를 엑세스하는 경우
 * 2. BatchPack : CSV, JSON, XML 등 파일의 형태로 한 번에 하나 이상의 이벤트 데이터를 엑세스 하는 경우
 *
 * 고려사항
 * 1. 다양한 에이전트와 연결이 가능해야 한다
 * -> 에이전트 식별자는 int, 에이전트 이름은 String 타입으로 정의한다
 * 2. 소스 시스템에서 수집 계층으로 가져오기 위해 데이터 변환이나 데이터 포맷 변환 과정을 크게 거치지 않을 수 있어야 한다
 * -> StreamPack의 데이터 타입은 Map<String,Object> 타입으로, BatchPack의 데이터 타입은 String 타입으로 정의한다
 */
@Getter
public abstract class AbstractPack {
    private Long time; // 데이터 수집 시간
    private Integer agentId; // 에이전트 식별자
    private String agentName; // 에이전트 이름
    private final Map<String, String> tags = new HashMap<>(); // 소스 시스템의 식별자

    public void updateTime(){
        this.time = DateUtil.now();
    }

    public void updateAgentInfo(Integer agentId, String agentName){
        this.agentId = agentId;
        this.agentName = agentName;
    }

    public void putTag(String key, String value){
        tags.put(key, value);
    }

    public String getTag(String key){
        return tags.getOrDefault(key, null);
    }


}
