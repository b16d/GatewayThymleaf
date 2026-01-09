package com.article.ai.gatewayfront.service;

import com.article.ai.gatewayfront.dto.AppResponse;

import java.io.IOException;
import java.util.List;

/**
 * Service interface for managing registered applications.
 */
public interface AppService {

    /**
     * Registers a remote Thymeleaf app by URL.
     *
     * @param appName the name of the application
     * @param description the description of the application
     * @param remoteBaseUrl the base URL of the remote application
     * @param username the username of the user registering the app
     * @return AppResponse containing the registered app details
     * @throws IllegalArgumentException if validation fails
     */
    AppResponse registerRemoteApp(String appName, String description, String remoteBaseUrl, String username);

    /**
     * Gets all active registered apps.
     *
     * @return list of all active apps
     */
    List<AppResponse> getAllApps();

    /**
     * Gets a specific app by ID.
     *
     * @param id the ID of the app
     * @return AppResponse containing the app details
     * @throws IllegalArgumentException if app not found
     */
    AppResponse getAppById(Long id);

    /**
     * Deletes an app.
     *
     * @param id the ID of the app to delete
     * @throws IllegalArgumentException if app not found
     * @throws IOException if deletion fails
     */
    void deleteApp(Long id) throws IOException;
}

