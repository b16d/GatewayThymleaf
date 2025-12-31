package com.article.ai.gatewayfront.controller;

import com.article.ai.gatewayfront.entity.RegisteredApp;
import com.article.ai.gatewayfront.repository.RegisteredAppRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AppControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegisteredAppRepository appRepository;

    @BeforeEach
    void setUp() {
        appRepository.deleteAll();
    }

    @Test
    void testHomePageWithoutAuth() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testHomePageWithAuth() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("app-list"));
    }

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testRegisterPageForbiddenForUser() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testRegisterPageAllowedForAdmin() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("app-register"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testRegisterRemoteApp() throws Exception {
        mockMvc.perform(post("/register")
                .param("appName", "RemoteTestApp")
                .param("description", "A remote test application")
                .param("appType", "REMOTE")
                .param("remoteBaseUrl", "https://example.com/app")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Verify app was created
        assert(appRepository.findByAppName("RemoteTestApp").isPresent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAppDetails() throws Exception {
        RegisteredApp app = new RegisteredApp("TestApp", "Test Description", RegisteredApp.AppType.REMOTE, "admin");
        app.setRemoteBaseUrl("https://example.com");
        RegisteredApp saved = appRepository.save(app);

        mockMvc.perform(get("/app/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("app-detail"))
                .andExpect(model().attributeExists("app"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllAppsApi() throws Exception {
        RegisteredApp app1 = new RegisteredApp("App1", "Description1", RegisteredApp.AppType.REMOTE, "admin");
        app1.setRemoteBaseUrl("https://example1.com");
        RegisteredApp app2 = new RegisteredApp("App2", "Description2", RegisteredApp.AppType.REMOTE, "admin");
        app2.setRemoteBaseUrl("https://example2.com");

        appRepository.save(app1);
        appRepository.save(app2);

        mockMvc.perform(get("/api/apps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appName").value("App1"))
                .andExpect(jsonPath("$[1].appName").value("App2"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteAppApi() throws Exception {
        RegisteredApp app = new RegisteredApp("DeleteMe", "Test", RegisteredApp.AppType.REMOTE, "admin");
        app.setRemoteBaseUrl("https://example.com");
        RegisteredApp saved = appRepository.save(app);

        mockMvc.perform(delete("/api/apps/" + saved.getId()))
                .andExpect(status().isOk());

        assert(appRepository.findById(saved.getId()).isEmpty());
    }
}

