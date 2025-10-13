package com.mssd.service.impl;

import com.mssd.dto.NewsletterDto;
import com.mssd.model.Newsletter;
import com.mssd.repository.NewsletterRepository;
import com.mssd.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsletterServiceImpl implements NewsletterService {
    private final NewsletterRepository newsletterRepository;

    @Override
    public void subscribe(NewsletterDto newsletterDto) {
        if (newsletterRepository.existsByEmail(newsletterDto.getEmail())) {
            throw new IllegalArgumentException("Email already subscribed");
        }
        Newsletter newsletter = new Newsletter();
        newsletter.setEmail(newsletterDto.getEmail());
        newsletterRepository.save(newsletter);
    }
} 