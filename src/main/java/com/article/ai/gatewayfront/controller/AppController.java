package com.article.ai.gatewayfront.controller;

import com.article.ai.gatewayfront.config.AppProperties;
import com.article.ai.gatewayfront.dto.AppRegistrationRequest;
import com.article.ai.gatewayfront.dto.AppResponse;
import com.article.ai.gatewayfront.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class AppController {

    private final AppService appService;
    private final AppProperties appProperties;

    @Autowired
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
     * Security: Protected by Spring Security - only ADMIN role can access /register
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registrationRequest", new AppRegistrationRequest());
        return "app-register";
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
     * Security: Protected by Spring Security - only ADMIN role can delete apps
     */
    @DeleteMapping("/api/apps/{id}")
    public ResponseEntity<?> deleteAppApi(@PathVariable Long id) {
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

