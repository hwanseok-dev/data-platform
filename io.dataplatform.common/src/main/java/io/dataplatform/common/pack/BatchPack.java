package io.dataplatform.common.pack;

public class BatchPack extends AbstractPack {
    private String fileName;
    private String document;

    // 비즈니스 메서드
    public void putResourceType(String resourceType){
        putTag("resourceType", resourceType);
    }

    // setter
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
