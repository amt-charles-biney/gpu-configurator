package com.amalitech.gpuconfigurator.dto.profile;

public interface ContactResponse {
    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);

    String getCountry();

    void setCountry(String country);

    String getIso2Code();

    void setIso2Code(String iso2Code);

    String getDialCode();

    void setDialCode(String dialCode);
}
