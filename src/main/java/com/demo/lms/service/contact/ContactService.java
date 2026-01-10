package com.demo.lms.service.contact;

import com.demo.lms.dto.request.CreateContactRequest;
import com.demo.lms.dto.request.UpdateContactStatusRequest;
import com.demo.lms.dto.response.ContactResponse;

import java.util.List;

public interface ContactService {

    ContactResponse createContact(Long userId, CreateContactRequest request);

    List<ContactResponse> getMyContacts(Long userId);

    List<ContactResponse> getAllContacts();

    ContactResponse updateContact(Long contactId, UpdateContactStatusRequest request);

    void deleteContact(Long contactId);
}
