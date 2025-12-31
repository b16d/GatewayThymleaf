package com.article.ai.gatewayfront.service;

import com.article.ai.gatewayfront.dto.AppResponse;
import com.article.ai.gatewayfront.entity.RegisteredApp;
import com.article.ai.gatewayfront.repository.RegisteredAppRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {

    @Mock
    private RegisteredAppRepository appRepository;

    @Mock
    private StorageService storageService;

    private AppService appService;

    @BeforeEach
    void setUp() {
        appService = new AppService(appRepository, storageService);
    }

    @Test
    void testRegisterRemoteApp_Success() {
        when(appRepository.findByAppName("TestApp")).thenReturn(Optional.empty());
        RegisteredApp savedApp = new RegisteredApp("TestApp", "Test Description", RegisteredApp.AppType.REMOTE, "admin");
        savedApp.setId(1L);
        savedApp.setRemoteBaseUrl("https://example.com");
        when(appRepository.save(any(RegisteredApp.class))).thenReturn(savedApp);

        AppResponse response = appService.registerRemoteApp("TestApp", "Test Description", "https://example.com", "admin");

        assertNotNull(response);
        assertEquals("TestApp", response.getAppName());
        assertEquals("REMOTE", response.getAppType());
        assertEquals("https://example.com", response.getRemoteBaseUrl());
        verify(appRepository, times(1)).save(any(RegisteredApp.class));
    }

    @Test
    void testRegisterRemoteApp_DuplicateName() {
        RegisteredApp existing = new RegisteredApp("TestApp", "Test", RegisteredApp.AppType.REMOTE, "admin");
        when(appRepository.findByAppName("TestApp")).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> {
            appService.registerRemoteApp("TestApp", "Test Description", "https://example.com", "admin");
        });
    }

    @Test
    void testRegisterRemoteApp_InvalidUrl() {
        when(appRepository.findByAppName("TestApp")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            appService.registerRemoteApp("TestApp", "Test", "invalid-url", "admin");
        });
    }

    @Test
    void testGetAllApps() {
        RegisteredApp app1 = new RegisteredApp("App1", "Desc1", RegisteredApp.AppType.REMOTE, "admin");
        app1.setId(1L);
        RegisteredApp app2 = new RegisteredApp("App2", "Desc2", RegisteredApp.AppType.REMOTE, "admin");
        app2.setId(2L);

        when(appRepository.findByActive(true)).thenReturn(List.of(app1, app2));

        List<AppResponse> apps = appService.getAllApps();

        assertEquals(2, apps.size());
        assertEquals("App1", apps.get(0).getAppName());
        assertEquals("App2", apps.get(1).getAppName());
    }

    @Test
    void testGetAppById_Success() {
        RegisteredApp app = new RegisteredApp("TestApp", "Test", RegisteredApp.AppType.REMOTE, "admin");
        app.setId(1L);
        when(appRepository.findById(1L)).thenReturn(Optional.of(app));

        AppResponse response = appService.getAppById(1L);

        assertNotNull(response);
        assertEquals("TestApp", response.getAppName());
    }

    @Test
    void testGetAppById_NotFound() {
        when(appRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            appService.getAppById(999L);
        });
    }

    @Test
    void testDeactivateApp() {
        RegisteredApp app = new RegisteredApp("TestApp", "Test", RegisteredApp.AppType.REMOTE, "admin");
        app.setId(1L);
        app.setActive(true);
        when(appRepository.findById(1L)).thenReturn(Optional.of(app));
        when(appRepository.save(any(RegisteredApp.class))).thenReturn(app);

        appService.deactivateApp(1L);

        verify(appRepository, times(1)).save(any(RegisteredApp.class));
        assertFalse(app.isActive());
    }
}

