package com.article.ai.gatewayfront.controller;

import com.article.ai.gatewayfront.dto.AppRegistrationRequest;
import com.article.ai.gatewayfront.dto.AppResponse;
import com.article.ai.gatewayfront.service.AppService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/v1/register")
public class RegisterController {

    private final AppService appService;

    public RegisterController(AppService appService) {
        this.appService = appService;
    }

    /**
     * Handle app registration form submission (admin only)
     * Security: Protected by Spring Security - only ADMIN role can access /api/v1/register/form
     * Endpoint: POST /api/v1/register/form
     */
    @PostMapping("/form")
    public String registerApp(
            @RequestParam String appName,
            @RequestParam String description,
            @RequestParam String remoteBaseUrl,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
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
     * REST API: Register remote app (PUBLIC - no authentication required)
     * Security: NOT protected - allows external app registration
     * Endpoint: POST /api/v1/register/remote
     */
    @PostMapping("/remote")
    @ResponseBody
    public ResponseEntity<?> registerRemoteAppApi(
            @RequestBody AppRegistrationRequest request) {

        try {
            AppResponse response = appService.registerRemoteApp(
                    request.appName(),
                    request.description(),
                    request.remoteBaseUrl(),
                    "anonymous"
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        }
    }
}
