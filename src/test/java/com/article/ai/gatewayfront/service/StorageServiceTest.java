package com.article.ai.gatewayfront.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {

    private StorageService storageService;

    @TempDir
    Path uploadDir;

    @BeforeEach
    void setUp() {
        storageService = new StorageService();
        ReflectionTestUtils.setField(storageService, "uploadDir", uploadDir.toString());
    }

    @Test
    void testExtractAndStoreApp_ValidZip() throws IOException {
        // Create a simple valid ZIP
        byte[] zipContent = createValidZip();

        MultipartFile zipFile = new MockMultipartFile(
                "zipFile",
                "test.zip",
                "application/zip",
                zipContent
        );

        String storagePath = storageService.extractAndStoreApp(1L, zipFile);

        assertNotNull(storagePath);
        assertTrue(Files.exists(Path.of(storagePath)));
    }

    @Test
    void testExtractAndStoreApp_EmptyFile() throws IOException {
        MultipartFile zipFile = new MockMultipartFile(
                "zipFile",
                "test.zip",
                "application/zip",
                new byte[0]
        );

        assertThrows(IllegalArgumentException.class, () -> {
            storageService.extractAndStoreApp(1L, zipFile);
        });
    }

    @Test
    void testExtractAndStoreApp_InvalidFileType() {
        MultipartFile txtFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "some content".getBytes()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            storageService.extractAndStoreApp(1L, txtFile);
        });
    }

    private byte[] createValidZip() throws IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry entry = new ZipEntry("templates/index.html");
            zos.putNextEntry(entry);
            zos.write("<html><body>Test</body></html>".getBytes());
            zos.closeEntry();
        }
        return baos.toByteArray();
    }
}

