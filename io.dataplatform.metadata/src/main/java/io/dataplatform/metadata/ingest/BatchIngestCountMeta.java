package io.dataplatform.metadata.ingest;

public class BatchIngestCountMeta {
    private static BatchIngestCountMeta instance ;

    public static synchronized BatchIngestCountMeta getInstance(){
        if (instance == null) {
            instance = new BatchIngestCountMeta();
        }
        return instance;
    }
    private BatchIngestCountMeta() {
    }

    /**
     * @param count 하나의 배치 파일에서 처리된 line의 수
     */
    public void add(Integer agentId, String agentName, String resourceType, int count){
        // TODO
    }


    /**
     * @param fileName 에러가 발생한 파일의 이름
     * @param count 에러가 발생한 line의 위치
     */
    public void addError(Integer agentId, String agentName, String resourceType, String fileName, int count){
    }
}
