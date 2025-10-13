package com.mssd.config;

import com.mssd.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FileStorageInitializer implements CommandLineRunner {
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize file storage directories
        fileStorageService.init();
    }
}