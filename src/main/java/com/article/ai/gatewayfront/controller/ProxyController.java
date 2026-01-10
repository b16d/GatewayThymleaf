package com.article.ai.gatewayfront.controller;

import com.article.ai.gatewayfront.entity.RegisteredApp;
import com.article.ai.gatewayfront.repository.RegisteredAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.view.RedirectView;

/**
 * Proxy controller for remote Thymeleaf apps.
 * Routes requests to the registered remote URL.
 */
@Controller
@RequestMapping("/proxy")
public class ProxyController {

    private final RegisteredAppRepository appRepository;

    @Autowired
    public ProxyController(RegisteredAppRepository appRepository) {
        this.appRepository = appRepository;
    }

    /**
     * Redirects to the remote app base URL.
     * For a remote app, we redirect the user instead of proxying.
     */
    @GetMapping("/{appId}/**")
    public RedirectView proxyToRemoteApp(@PathVariable Long appId) {
        RegisteredApp app = appRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("App not found: " + appId));

        if (app.getAppType() != RegisteredApp.AppType.REMOTE) {
            throw new IllegalArgumentException("This app is not a remote app");
        }

        // Redirect to the remote base URL
        return new RedirectView(app.getRemoteBaseUrl());
    }
}

