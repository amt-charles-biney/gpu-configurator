package com.amalitech.gpuconfigurator.dto.profile;

public interface BasicInformationResponse {
    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String email);

    ContactResponse getContact();

    void setContact(ContactResponse contactResponse);
}
