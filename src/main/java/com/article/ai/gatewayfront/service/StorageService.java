package com.article.ai.gatewayfront.service;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

@Service
public class StorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final long MAX_ENTRY_SIZE = 100 * 1024; // 100 KB per file

    /**
     * Validates and extracts a ZIP file for an uploaded app.
     * Performs path traversal checks and size validation.
     */
    public String extractAndStoreApp(Long appId, MultipartFile zipFile) throws IOException {
        // Validate file
        if (zipFile == null || zipFile.isEmpty()) {
            throw new IllegalArgumentException("ZIP file is empty");
        }

        if (zipFile.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 10MB");
        }

        if (!zipFile.getContentType().contains("zip") && !zipFile.getOriginalFilename().endsWith(".zip")) {
            throw new IllegalArgumentException("File must be a ZIP archive");
        }

        // Create app directory
        Path appDir = Paths.get(uploadDir, "app-" + appId);
        Files.createDirectories(appDir);

        // Extract ZIP with security checks
        extractZipSecurely(zipFile, appDir);

        return appDir.toString();
    }

    /**
     * Securely extracts ZIP entries, checking for path traversal attacks.
     */
    private void extractZipSecurely(MultipartFile zipFile, Path targetDir) throws IOException {
        File tempZip = File.createTempFile("upload_", ".zip");
        zipFile.transferTo(tempZip);

        try (ZipFile zf = new ZipFile(tempZip)) {
            Enumeration<ZipArchiveEntry> entries = zf.getEntries();

            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();

                // Security: Prevent path traversal
                if (entry.getName().contains("..") || entry.getName().startsWith("/")) {
                    throw new SecurityException("Invalid path in ZIP: " + entry.getName());
                }

                Path entryPath = targetDir.resolve(entry.getName()).normalize();

                // Ensure the resolved path is still within targetDir
                if (!entryPath.startsWith(targetDir.normalize())) {
                    throw new SecurityException("Path traversal attempt detected");
                }

                // Check file size
                if (entry.getSize() > MAX_ENTRY_SIZE && entry.getSize() > 0) {
                    throw new IllegalArgumentException("File in ZIP exceeds max size: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    try (InputStream is = zf.getInputStream(entry)) {
                        Files.copy(is, entryPath);
                    }
                }
            }
        } finally {
            Files.deleteIfExists(tempZip.toPath());
        }
    }

    /**
     * Deletes the stored files for an app.
     */
    public void deleteAppStorage(String storagePath) throws IOException {
        if (storagePath != null && !storagePath.isEmpty()) {
            Path path = Paths.get(storagePath);
            deleteDirectoryRecursively(path);
        }
    }

    /**
     * Recursively deletes a directory and its contents.
     */
    private void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a)) // Delete files before dirs
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            // Log and continue
                        }
                    });
        }
    }

    /**
     * Gets the storage path for an uploaded app.
     */
    public Path getAppStoragePath(Long appId) {
        return Paths.get(uploadDir, "app-" + appId);
    }
}

