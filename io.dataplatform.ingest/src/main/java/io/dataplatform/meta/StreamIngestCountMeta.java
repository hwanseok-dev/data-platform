package io.dataplatform.meta;

public class StreamIngestCountMeta implements CountMeta {
    private static StreamIngestCountMeta instance ;

    public static synchronized StreamIngestCountMeta getInstance(){
        if (instance == null) {
            instance = new StreamIngestCountMeta();
        }
        return instance;
    }
    private StreamIngestCountMeta() {
    }

    @Override
    public void addCount(Integer agentId, String agentName, String resourceType) {

    }

    @Override
    public void addErrorCount(Integer agentId, String agentName, String resourceType) {

    }
}
