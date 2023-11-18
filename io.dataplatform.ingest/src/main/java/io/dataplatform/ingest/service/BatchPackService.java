package io.dataplatform.ingest.service;

import io.dataplatform.common.pack.BatchPack;
import io.dataplatform.common.ref.INT;
import io.dataplatform.ingest.reader.CommonDecompressionStrategy;
import io.dataplatform.ingest.reader.DecompressionStrategy;
import io.dataplatform.ingest.reader.GzipDecompressionStrategy;
import io.dataplatform.meta.BatchIngestCountMeta;
import io.dataplatform.process.pre.BatchPackCore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

@Service
@Slf4j
public class BatchPackService {

    /**
     * 1. 파일 압축 해제
     * 2. 각 라인을 BatchPack으로 변환
     * 3. BatchPackCore로 전송
     * 4. BatchIngestMeta 추가
     */
    public void process(Integer agentId, String agentName, String resourceType, MultipartFile file){
        // split line by line
        String fileName = file.getName();
        DecompressionStrategy decompressor = chooseDecompressor(file.getName());
        Function<String, BatchPack> generator = chooseBatchPackGenerator(resourceType, agentId, agentName, fileName);
        INT count = new INT();
        try (InputStream is = file.getInputStream();
             BufferedReader reader = decompressor.getBufferedReader(is)) {
            String line = reader.readLine();
            while (line != null) {
                // convert line to pack
                BatchPack pack = generator.apply(line);
                // add to core
                BatchPackCore.getInstance().add(pack);
                count.increase();
                line = reader.readLine();
            }
            // add meta
            BatchIngestCountMeta.getInstance().add(agentId, agentName, resourceType, count.getValue());
        } catch (IOException e) {
            // 같은 배치 파일에 대해서는 에러가 한 번만 발생해도 전체 배치 파일의 처리를 중단한다
            log.error("fileName : {}, errMsg : {}, lineCnt : {}", fileName, e.getMessage(), count.getValue());
            BatchIngestCountMeta.getInstance().addError(agentId, agentName, resourceType, fileName, count.getValue());
        }
    }
    private DecompressionStrategy chooseDecompressor(String fileName){
        if (fileName.endsWith(".gz")) {
            return new GzipDecompressionStrategy();
        }
        return new CommonDecompressionStrategy();
    }

    private Function<String, BatchPack> chooseBatchPackGenerator(String resourceType, Integer agentId, String agentName, String fileName){
        String uppercase = resourceType.toUpperCase();
        switch (uppercase) {
            case "VPC" :
            case "ELB" :
            case "CLOUD_FRONT":
            case "WAF":
                return line -> {
                    BatchPack batchPack = new BatchPack();
                    batchPack.updateTime();
                    batchPack.updateAgentInfo(agentId, agentName);
                    batchPack.setFileName(fileName);
                    batchPack.setDocument(line);
                    batchPack.putResourceType(uppercase);
                    return batchPack;
                };
            default:
                return line -> {
                    BatchPack batchPack = new BatchPack();
                    batchPack.updateTime();
                    batchPack.updateAgentInfo(agentId, agentName);
                    batchPack.setFileName(fileName);
                    batchPack.setDocument(line);
                    batchPack.putResourceType("COMMON");
                    return batchPack;
                };
        }
    }
}
