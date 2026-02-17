package com.mssd.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class FileController {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    // List available images in the upload directory and some asset images
    @GetMapping("/images")
    public ResponseEntity<List<Map<String, Object>>> getAvailableImages() {
        List<Map<String, Object>> imageList = new ArrayList<>();
        try {
            Path uploadsPath = Paths.get(uploadDir);
            if (Files.exists(uploadsPath)) {
                try (Stream<Path> files = Files.walk(uploadsPath, 1)) {
                    files.filter(Files::isRegularFile)
                         .filter(this::isImageFile)
                         .forEach(file -> {
                             Map<String, Object> imageInfo = new HashMap<>();
                             imageInfo.put("name", file.getFileName().toString());
                             imageInfo.put("path", file.getFileName().toString());
                             imageInfo.put("type", "uploaded");
                             imageInfo.put("size", getFileSize(file));
                             imageList.add(imageInfo);
                         });
                }
            }

            // Add a few known asset images if they exist in classpath
            String[] commonAssets = {
                "portfolio-1.jpg", "portfolio-2.jpg", "portfolio-3.jpg",
                "portfolio-4.jpg", "portfolio-5.jpg", "portfolio-6.jpg"
            };
            for (String assetName : commonAssets) {
                Resource assetResource = new ClassPathResource("static/assets/img/" + assetName);
                if (assetResource.exists()) {
                    Map<String, Object> imageInfo = new HashMap<>();
                    imageInfo.put("name", assetName);
                    imageInfo.put("path", "assets/img/" + assetName);
                    imageInfo.put("type", "asset");
                    imageInfo.put("size", "N/A");
                    imageList.add(imageInfo);
                }
            }

            log.info("Found {} available images", imageList.size());
            return ResponseEntity.ok(imageList);
        } catch (Exception e) {
            log.error("Error listing available images: {}", e.getMessage());
            return ResponseEntity.status(500).body(imageList);
        }
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename);
            if (Files.exists(file)) {
                Files.delete(file);
                log.info("File deleted successfully: {}", filename);
                return ResponseEntity.ok(Map.of("message", "File deleted successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            log.error("Error deleting file: {}", e.getMessage());
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Could not delete file: " + e.getMessage()));
        }
    }

    private boolean isImageFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
               fileName.endsWith(".png") || fileName.endsWith(".gif") ||
               fileName.endsWith(".svg") || fileName.endsWith(".webp");
    }

    private String getFileSize(Path file) {
        try {
            long bytes = Files.size(file);
            if (bytes < 1024) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(1024));
            String[] units = {"B", "KB", "MB", "GB"};
            return String.format("%.1f %s", bytes / Math.pow(1024, exp), units[exp]);
        } catch (IOException e) {
            return "Unknown";
        }
    }
}
