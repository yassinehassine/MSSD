package com.mssd.service;

import com.mssd.dto.ContactDto;

import java.util.List;

public interface ContactService {
    void submitContact(ContactDto contactDto);
    List<ContactDto> getAllContacts();
} 