package com.mssd.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cloudinary configuration for cloud-based image storage.
 * Only activated when ALL three Cloudinary env vars are set to non-empty values (production on Render).
 * In local dev (no env vars), the bean is NOT created and file uploads fall back to local storage.
 *
 * Get free credentials at: https://cloudinary.com
 */
@Configuration
public class CloudinaryConfig {

    @Bean
    @ConditionalOnExpression(
        "!'${cloudinary.cloud-name:}'.isEmpty() && " +
        "!'${cloudinary.api-key:}'.isEmpty() && " +
        "!'${cloudinary.api-secret:}'.isEmpty()"
    )
    public Cloudinary cloudinary(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
        ));
    }
}
