package io.dataplatform.common.pack;

public class BatchPack extends AbstractPack {
    private String fileName;
    private String line;

    // 생성자
    public BatchPack(Long time, Integer agentId) {
        super(time, agentId);
    }

    public BatchPack(Long time, Integer agentId, String agentName) {
        super(time, agentId, agentName);
    }

    // 비즈니스 메서드
    public void putResourceType(String resourceType){
        putTag("resourceType", resourceType);
    }

    // setter
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
