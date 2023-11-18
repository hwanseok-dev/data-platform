package io.dataplatform.ingest.controller;

import io.dataplatform.common.pack.StreamPack;
import io.dataplatform.ingest.exception.BadAgentException;
import io.dataplatform.ingest.service.BatchPackService;
import io.dataplatform.ingest.service.StreamPackService;
import io.dataplatform.ingest.util.HttpHeaderUtil;
import io.dataplatform.meta.BatchIngestCountMeta;
import io.dataplatform.meta.StreamIngestCountMeta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class IngestController {

    private final StreamPackService streamPackService;

    private final BatchPackService batchPackService;

    @PostMapping("/api/v1/ingest/stream")
    public ResponseEntity<Boolean> ingestStream(@RequestBody StreamPack pack,
                                                HttpServletRequest request) {
        Integer agentId = null;
        String agentName = null;
        String resourceType = null;
        try {
            agentId = HttpHeaderUtil.getOrThrowAgentId(request);
            agentName = HttpHeaderUtil.getOrThrowAgentName(request);
            resourceType = HttpHeaderUtil.getOrThrowResourceType(request);
            streamPackService.process(agentId, agentName, resourceType, pack);
        } catch (BadAgentException e) {
            log.error("agentId: {}, agentName: {}, resourceType: {}", agentId, agentName, resourceType);
            StreamIngestCountMeta.getInstance().addErrorCount(agentId, agentName, resourceType);
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/api/v1/ingest/batch")
    public ResponseEntity<Boolean> ingestBatch(@RequestParam(value = "file") MultipartFile file,
                                               HttpServletRequest request) {
        Integer agentId = null;
        String agentName = null;
        String resourceType = null;
        try {
            agentId = HttpHeaderUtil.getOrThrowAgentId(request);
            agentName = HttpHeaderUtil.getOrThrowAgentName(request);
            resourceType = HttpHeaderUtil.getOrThrowResourceType(request);
            batchPackService.process(agentId, agentName, resourceType, file);
        } catch (BadAgentException e) {
            log.error("agentId: {}, agentName: {}, resourceType: {}, fileName : {}", agentId, agentName, resourceType, file.getName());
            BatchIngestCountMeta.getInstance().addError(agentId, agentName, resourceType, file.getName(), 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            BatchIngestCountMeta.getInstance().addError(agentId, agentName, resourceType, file.getName(), 0);
        }
        return ResponseEntity.ok(true);
    }
}
