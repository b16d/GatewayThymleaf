package com.article.ai.gatewayfront.controller;

import com.article.ai.gatewayfront.dto.AppResponse;
import com.article.ai.gatewayfront.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * REST API Controller for App management
 * All endpoints are under /api/v1/app
 */
@RestController
@RequestMapping("/api/v1/app")
public class AppApiController {

    private final AppService appService;

    @Autowired
    public AppApiController(AppService appService) {
        this.appService = appService;
    }

    /**
     * Get all apps
     * Public endpoint
     */
    @GetMapping
    public ResponseEntity<List<AppResponse>> getAllApps() {
        List<AppResponse> apps = appService.getAllApps();
        return ResponseEntity.ok(apps);
    }

    /**
     * Get app by ID
     * Public endpoint
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppById(@PathVariable Long id) {
        try {
            AppResponse app = appService.getAppById(id);
            return ResponseEntity.ok(app);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("App not found: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving app: " + e.getMessage());
        }
    }

    /**
     * Delete app by ID
     * ADMIN only endpoint
     * Security: Protected by Spring Security - only ADMIN role can delete apps
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApp(@PathVariable Long id) {
        try {
            appService.deleteApp(id);
            return ResponseEntity.ok("App deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("App not found: " + id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete app: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting app: " + e.getMessage());
        }
    }
}

