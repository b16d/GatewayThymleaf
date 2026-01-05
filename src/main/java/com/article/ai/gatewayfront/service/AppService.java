package com.article.ai.gatewayfront.service;

import com.article.ai.gatewayfront.dto.AppResponse;
import com.article.ai.gatewayfront.entity.RegisteredApp;
import com.article.ai.gatewayfront.repository.RegisteredAppRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppService {

    private final RegisteredAppRepository appRepository;

    public AppService(RegisteredAppRepository appRepository) {
        this.appRepository = appRepository;
    }


    /**
     * Registers a remote Thymeleaf app by URL.
     */
    public AppResponse registerRemoteApp(String appName, String description, String remoteBaseUrl, String username) {
        // Validate URL
        if (remoteBaseUrl == null || remoteBaseUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Remote URL cannot be empty");
        }

        if (!remoteBaseUrl.startsWith("http://") && !remoteBaseUrl.startsWith("https://")) {
            throw new IllegalArgumentException("Remote URL must start with http:// or https://");
        }

        // Check if app name already exists
        if (appRepository.findByAppName(appName).isPresent()) {
            throw new IllegalArgumentException("App name already exists: " + appName);
        }

        RegisteredApp app = new RegisteredApp(appName, description, RegisteredApp.AppType.REMOTE, username);
        app.setRemoteBaseUrl(remoteBaseUrl);

        RegisteredApp saved = appRepository.save(app);
        return mapToResponse(saved);
    }

    /**
     * Gets all active registered apps.
     */
    public List<AppResponse> getAllApps() {
        return appRepository.findByActive(true).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Gets a specific app by ID.
     */
    public AppResponse getAppById(Long id) {
        RegisteredApp app = appRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("App not found: " + id));
        return mapToResponse(app);
    }


    /**
     * Deletes an app.
     */
    public void deleteApp(Long id) throws IOException {
        RegisteredApp app = appRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("App not found: " + id));


        appRepository.delete(app);
    }

    /**
     * Maps entity to DTO response.
     */
    private AppResponse mapToResponse(RegisteredApp app) {
        return new AppResponse(
                app.getId(),
                app.getAppName(),
                app.getDescription(),
                app.getAppType().toString(),
                app.getRemoteBaseUrl(),
                app.getCreatedAt(),
                app.getCreatedBy(),
                app.isActive()
        );
    }
}

