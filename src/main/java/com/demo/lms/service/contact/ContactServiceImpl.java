package com.demo.lms.service.contact;

import com.demo.lms.dto.request.CreateContactRequest;
import com.demo.lms.dto.request.UpdateContactStatusRequest;
import com.demo.lms.dto.response.ContactResponse;
import com.demo.lms.exception.ContactNotFoundException;
import com.demo.lms.exception.UserNotFoundException;
import com.demo.lms.model.entity.Contact;
import com.demo.lms.model.entity.User;
import com.demo.lms.model.enums.ContactStatus;
import com.demo.lms.repository.ContactRepository;
import com.demo.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ContactResponse createContact(Long userId, CreateContactRequest request) {
        log.debug("Creating contact for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setSubject(request.getSubject());
        contact.setMessage(request.getMessage());
        contact.setStatus(ContactStatus.OPEN);

        Contact savedContact = contactRepository.save(contact);
        log.info("Contact created successfully with ID: {}", savedContact.getId());

        return map(savedContact);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> getMyContacts(Long userId) {
        log.debug("Fetching contacts for user ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        return contactRepository.findByUserId(userId)
                .stream().map(this::map).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> getAllContacts() {
        log.debug("Fetching all contacts");

        return contactRepository.findAll()
                .stream().map(this::map).toList();
    }

    @Override
    @Transactional
    public ContactResponse updateContact(Long contactId, UpdateContactStatusRequest request) {
        log.debug("Updating contact with ID: {}", contactId);

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException(contactId));

        contact.setStatus(request.getStatus());
        contact.setAdminResponse(request.getAdminResponse());
        contact.setRespondedAt(LocalDateTime.now());

        Contact savedContact = contactRepository.save(contact);
        log.info("Contact updated successfully with ID: {}", savedContact.getId());

        return map(savedContact);
    }

    @Override
    @Transactional
    public void deleteContact(Long contactId) {
        log.debug("Deleting contact with ID: {}", contactId);

        if (!contactRepository.existsById(contactId)) {
            throw new ContactNotFoundException(contactId);
        }

        contactRepository.deleteById(contactId);
        log.info("Contact deleted successfully with ID: {}", contactId);
    }

    private ContactResponse map(Contact contact) {
        ContactResponse res = new ContactResponse();
        res.setId(contact.getId());
        res.setSubject(contact.getSubject());
        res.setMessage(contact.getMessage());
        res.setStatus(contact.getStatus());
        res.setAdminResponse(contact.getAdminResponse());
        res.setCreatedAt(contact.getCreatedAt());
        res.setRespondedAt(contact.getRespondedAt());

        if (contact.getUser() != null) {
            res.setUserName(contact.getUser().getName());
            res.setUserEmail(contact.getUser().getEmail());
        }

        return res;
    }
}
