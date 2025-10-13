package com.mssd.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    @Value("${app.upload.dir:src/main/resources/static/uploads}")
    private String uploadDir;

    /**
     * Save uploaded file and return the relative URL path
     * @param file MultipartFile to save
     * @return String relative URL path (e.g., "uploads/filename.jpg")
     * @throws IOException if file saving fails
     */
    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Validate file type (images only)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID() + fileExtension;

        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return relative URL path
        return "uploads/" + filename;
    }

    /**
     * Delete a file by its relative URL path
     * @param fileUrl String relative URL path (e.g., "uploads/filename.jpg")
     * @return boolean true if file was deleted, false otherwise
     */
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            // Extract filename from URL path
            String filename;
            if (fileUrl.startsWith("uploads/")) {
                filename = fileUrl.substring("uploads/".length());
            } else {
                // Handle case where fileUrl is just the filename
                filename = fileUrl;
            }

            Path filePath = Paths.get(uploadDir).resolve(filename);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return true;
            }
        } catch (Exception e) {
            // Log error but don't throw exception to avoid breaking the delete operation
            System.err.println("Failed to delete file: " + fileUrl + ", Error: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Check if a file exists
     * @param fileUrl String relative URL path (e.g., "uploads/filename.jpg")
     * @return boolean true if file exists, false otherwise
     */
    public boolean fileExists(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            String filename;
            if (fileUrl.startsWith("uploads/")) {
                filename = fileUrl.substring("uploads/".length());
            } else {
                filename = fileUrl;
            }

            Path filePath = Paths.get(uploadDir).resolve(filename);
            return Files.exists(filePath);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the absolute path for a file URL
     * @param fileUrl String relative URL path (e.g., "uploads/filename.jpg")
     * @return Path absolute path to the file, or null if invalid
     */
    public Path getFilePath(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }

        try {
            String filename;
            if (fileUrl.startsWith("uploads/")) {
                filename = fileUrl.substring("uploads/".length());
            } else {
                filename = fileUrl;
            }

            return Paths.get(uploadDir).resolve(filename);
        } catch (Exception e) {
            return null;
        }
    }
}