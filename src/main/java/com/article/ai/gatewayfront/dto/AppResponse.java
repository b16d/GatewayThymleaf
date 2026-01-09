package com.article.ai.gatewayfront.dto;

import java.time.LocalDateTime;

public record AppResponse(
        Long id,
        String appName,
        String description,
        String appType,
        String remoteBaseUrl,
        LocalDateTime createdAt,
        String createdBy,
        boolean active
) {}

