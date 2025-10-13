package com.mssd.service.impl;

import com.mssd.dto.ContactDto;
import com.mssd.model.Contact;
import com.mssd.repository.ContactRepository;
import com.mssd.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    @Override
    public void submitContact(ContactDto contactDto) {
        Contact contact = new Contact();
        contact.setFullName(contactDto.getFullName());
        contact.setEmail(contactDto.getEmail());
        contact.setPhone(contactDto.getPhone());
        contact.setSubject(contactDto.getSubject());
        contact.setMessage(contactDto.getMessage());
        contactRepository.save(contact);
    }

    @Override
    public List<ContactDto> getAllContacts() {
        return contactRepository.findAll().stream().map(contact -> new ContactDto(
            contact.getFullName(),
            contact.getEmail(),
            contact.getPhone(),
            contact.getSubject(),
            contact.getMessage()
        )).collect(Collectors.toList());
    }
} 