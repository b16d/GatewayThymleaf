package com.article.ai.gatewayfront.dto;

public record AppRegistrationRequest(
        String appName,
        String description,
        String appType, // "UPLOADED" or "REMOTE"
        String remoteBaseUrl // For REMOTE type
) {
    // Default constructor for form binding
    public AppRegistrationRequest() {
        this(null, null, null, null);
    }
}

