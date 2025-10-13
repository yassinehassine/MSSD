package com.mssd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
            
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files (backward compat and unified path)
        registry.addResourceHandler("/api/portfolio/files/**", "/api/files/**")
            .addResourceLocations("file:" + uploadDir + "/");

        // Serve static resources
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
        }
        };
    }
} 