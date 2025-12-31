package com.article.ai.gatewayfront.dto;

public class AppRegistrationRequest {
    private String appName;
    private String description;
    private String appType; // "UPLOADED" or "REMOTE"
    private String remoteBaseUrl; // For REMOTE type

    // Constructors
    public AppRegistrationRequest() {}

    public AppRegistrationRequest(String appName, String description, String appType) {
        this.appName = appName;
        this.description = description;
        this.appType = appType;
    }

    // Getters & Setters
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getRemoteBaseUrl() {
        return remoteBaseUrl;
    }

    public void setRemoteBaseUrl(String remoteBaseUrl) {
        this.remoteBaseUrl = remoteBaseUrl;
    }
}

