package io.dataplatform.ingest.util;

import io.dataplatform.ingest.exception.BadAgentException;

import javax.servlet.http.HttpServletRequest;

public class HttpHeaderUtil {
    private HttpHeaderUtil() {}

    public static Integer getOrThrowAgentId(HttpServletRequest request) {
        try {
            String agentId = request.getHeader("agentId");
            return Integer.parseInt(agentId);
        } catch (NumberFormatException e) {
            throw new BadAgentException("agentId not found");
        }
    }

    public static String getOrThrowAgentName(HttpServletRequest request) {
        String agentName = request.getHeader("agentName");
        if (agentName != null) {
            return agentName;
        }
        throw new BadAgentException("agentName not found");
    }

    public static String getOrThrowResourceType(HttpServletRequest request) {
        String resourceName = request.getHeader("resourceType");
        if (resourceName != null) {
            return resourceName;
        }
        throw new BadAgentException("resourceType not found");
    }
}
