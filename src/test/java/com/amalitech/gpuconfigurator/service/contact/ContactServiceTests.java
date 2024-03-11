package com.amalitech.gpuconfigurator.service.contact;

import com.amalitech.gpuconfigurator.dto.profile.ContactRequest;
import com.amalitech.gpuconfigurator.model.Contact;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.repository.ContactRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTests {
    @InjectMocks
    private ContactServiceImpl contactService;

    @Mock
    private ContactRepository contactRepository;

    private ContactRequest contactRequestDto;

    private User user;

    @BeforeEach
    public void init() {
        user = new User();
        contactRequestDto = ContactRequest.builder()
                .phoneNumber("0245678901")
                .country("Ghana")
                .iso2Code("GH")
                .dialCode("+233")
                .build();
    }

    @Test
    public void createOrUpdate_whenValidInput_returnsSavedContact() {
        Contact contact = Contact.builder()
                .phoneNumber(contactRequestDto.getPhoneNumber())
                .country(contactRequestDto.getCountry())
                .iso2Code(contactRequestDto.getIso2Code())
                .dialCode(contactRequestDto.getDialCode())
                .build();

        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        Contact contactResult = contactService.createOrUpdate(user, contactRequestDto);

        verify(contactRepository, times(1)).save(any(Contact.class));
        Assertions.assertThat(contact).isEqualTo(contactResult);
        Assertions.assertThat(contact.getPhoneNumber()).isEqualTo(contactRequestDto.getPhoneNumber());
        Assertions.assertThat(contact.getCountry()).isEqualTo(contactRequestDto.getCountry());
        Assertions.assertThat(contact.getIso2Code()).isEqualTo(contactRequestDto.getIso2Code());
        Assertions.assertThat(contact.getDialCode()).isEqualTo(contactRequestDto.getDialCode());
    }

    @Test
    public void createOrUpdate_whenExistingContactAndValidInput_shouldUpdateContactAndReturnsUpdatedContact() {
        Contact existingContact = Contact.builder()
                .phoneNumber("0201234567")
                .country("Ghana")
                .iso2Code("GH")
                .dialCode("+233")
                .build();
        user.setContact(existingContact);
        Contact updatedContact = Contact.builder()
                .id(existingContact.getId())
                .phoneNumber(contactRequestDto.getPhoneNumber())
                .country(contactRequestDto.getCountry())
                .iso2Code(contactRequestDto.getIso2Code())
                .dialCode(contactRequestDto.getDialCode())
                .build();

        when(contactRepository.save(any(Contact.class))).thenReturn(updatedContact);

        Contact contactResult = contactService.createOrUpdate(user, contactRequestDto);

        verify(contactRepository, times(1)).save(any(Contact.class));
        Assertions.assertThat(updatedContact).isEqualTo(contactResult);
        Assertions.assertThat(updatedContact.getId()).isEqualTo(existingContact.getId());
        Assertions.assertThat(updatedContact.getPhoneNumber()).isEqualTo(contactRequestDto.getPhoneNumber());
        Assertions.assertThat(updatedContact.getCountry()).isEqualTo(contactRequestDto.getCountry());
        Assertions.assertThat(updatedContact.getIso2Code()).isEqualTo(contactRequestDto.getIso2Code());
        Assertions.assertThat(updatedContact.getDialCode()).isEqualTo(contactRequestDto.getDialCode());
    }
}