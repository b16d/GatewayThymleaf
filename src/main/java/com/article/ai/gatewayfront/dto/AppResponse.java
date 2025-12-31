package com.article.ai.gatewayfront.dto;

import java.time.LocalDateTime;

public class AppResponse {
    private Long id;
    private String appName;
    private String description;
    private String appType;
    private String remoteBaseUrl;
    private LocalDateTime createdAt;
    private String createdBy;
    private boolean active;

    // Constructors
    public AppResponse() {}

    public AppResponse(Long id, String appName, String description, String appType,
                       String remoteBaseUrl, LocalDateTime createdAt, String createdBy, boolean active) {
        this.id = id;
        this.appName = appName;
        this.description = description;
        this.appType = appType;
        this.remoteBaseUrl = remoteBaseUrl;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.active = active;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

