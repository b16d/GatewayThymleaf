package com.article.ai.gatewayfront.controller;

import com.article.ai.gatewayfront.config.AppProperties;
import com.article.ai.gatewayfront.dto.AppRegistrationRequest;
import com.article.ai.gatewayfront.dto.AppResponse;
import com.article.ai.gatewayfront.service.AppService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class AppController {

    private final AppService appService;
    private final AppProperties appProperties;

    public AppController(AppService appService, AppProperties appProperties) {
        this.appService = appService;
        this.appProperties = appProperties;
    }

    /**
     * Home page - displays list of registered apps
     */
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        // Add app properties to model
        model.addAttribute("appTitle", appProperties.getTitle());
        model.addAttribute("appDescription", appProperties.getDescription());
        List<AppResponse> apps = appService.getAllApps();
        model.addAttribute("apps", apps);

        // Add authentication info to model
        if (authentication != null) {
            model.addAttribute("isAuthenticated", authentication.isAuthenticated());
            model.addAttribute("isAdmin", authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        } else {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("isAdmin", false);
        }

        return "app-list";
    }

    /**
     * Login page
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * App registration page (admin only)
     */
    @GetMapping("/register")
    public String registerPage(Authentication authentication, Model model) {
        // Check if user is authenticated and has ADMIN role
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/login";
        }
        model.addAttribute("registrationRequest", new AppRegistrationRequest());
        return "app-register";
    }

    /**
     * Handle app registration form submission (admin only)
     */
    @PostMapping("/register")
    public String registerApp(
            @RequestParam String appName,
            @RequestParam String description,
            @RequestParam String remoteBaseUrl,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            // Verify user is ADMIN
            if (authentication == null || !authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                redirectAttributes.addFlashAttribute("error", "Only ADMIN users can register apps");
                return "redirect:/login";
            }

            String username = authentication.getName();
            appService.registerRemoteApp(appName, description, remoteBaseUrl, username);
            redirectAttributes.addFlashAttribute("message", "App registered successfully: " + appName);
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * View app details
     */
    @GetMapping("/app/{id}")
    public String viewApp(@PathVariable Long id, Model model) {
        try {
            AppResponse app = appService.getAppById(id);
            model.addAttribute("app", app);
            return "app-detail";
        } catch (Exception e) {
            model.addAttribute("error", "App not found");
            return "redirect:/";
        }
    }


    /**
     * REST API: Register remote app
     */
    @PostMapping("/api/apps/register/remote")
    public ResponseEntity<?> registerRemoteAppApi(
            @RequestBody AppRegistrationRequest request,
            Authentication authentication) {

        try {
            String username = authentication.getName();
            AppResponse response = appService.registerRemoteApp(
                    request.getAppName(),
                    request.getDescription(),
                    request.getRemoteBaseUrl(),
                    username
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        }
    }

    /**
     * REST API: Get all apps
     */
    @GetMapping("/api/apps")
    public ResponseEntity<List<AppResponse>> getAllAppsApi() {
        List<AppResponse> apps = appService.getAllApps();
        return ResponseEntity.ok(apps);
    }

    /**
     * REST API: Get app by ID
     */
    @GetMapping("/api/apps/{id}")
    public ResponseEntity<?> getAppApi(@PathVariable Long id) {
        try {
            AppResponse app = appService.getAppById(id);
            return ResponseEntity.ok(app);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("App not found");
        }
    }

    /**
     * REST API: Delete app (ADMIN only)
     */
    @DeleteMapping("/api/apps/{id}")
    public ResponseEntity<?> deleteAppApi(@PathVariable Long id, Authentication authentication) {
        // Check if user is authenticated and has ADMIN role
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Only ADMIN users can delete apps");
        }

        try {
            appService.deleteApp(id);
            return ResponseEntity.ok("App deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete app");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("App not found");
        }
    }
}

