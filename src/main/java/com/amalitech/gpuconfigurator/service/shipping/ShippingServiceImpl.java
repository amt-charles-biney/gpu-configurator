package com.amalitech.gpuconfigurator.service.shipping;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.profile.ContactRequest;
import com.amalitech.gpuconfigurator.dto.shipping.AddressRequestDto;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingRequest;
import com.amalitech.gpuconfigurator.dto.shipping.ShippingResponse;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Contact;
import com.amalitech.gpuconfigurator.model.Shipping;
import com.amalitech.gpuconfigurator.model.User;
import com.amalitech.gpuconfigurator.model.UserSession;
import com.amalitech.gpuconfigurator.repository.ShippingRepository;
import com.amalitech.gpuconfigurator.repository.UserRepository;
import com.amalitech.gpuconfigurator.repository.UserSessionRepository;
import com.amalitech.gpuconfigurator.service.verifyAddress.AddressVerification;
import com.easypost.exception.EasyPostException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {
    private final ShippingRepository shippingRepository;
    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;
    private final AddressVerification addressVerification;

    @Transactional
    @Override
    public ShippingResponse create(ShippingRequest dto, User currentUser, UserSession userSession) throws EasyPostException {
        addressVerification.verifyAddress(mapShippingRequestToAddressRequestDto(dto));

        Shipping newShipping = mapShippingRequestToShipping(dto);

        Shipping savedShipping = shippingRepository.save(newShipping);

        if (currentUser != null && dto.isSaveInformation()) {
            currentUser.setShippingInformation(savedShipping);
            userRepository.save(currentUser);
        }

        userSession.setCurrentShipping(savedShipping);
        userSessionRepository.save(userSession);

        return shippingRepository.findShippingResponseById(savedShipping.getId());
    }

    @Override
    public Page<ShippingResponse> findAll(int page, int size) {
        return shippingRepository.findAllBy(PageRequest.of(page, size));
    }

    @Override
    public ShippingResponse findById(UUID shippingId) {
        return shippingRepository.findOptionalShippingResponseById(shippingId)
                .orElseThrow(() -> new NotFoundException("Shipping information with id " + shippingId + " not found"));
    }

    @Override
    public ShippingResponse update(UUID shippingId, ShippingRequest dto) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new NotFoundException("Shipping information with id " + shippingId + " not found"));

        Shipping updatedShipping = mapShippingRequestToShipping(dto);
        updatedShipping.setId(shipping.getId());

        Contact updatedContact = updatedShipping.getContact();
        updatedContact.setId(shipping.getContact().getId());
        updatedShipping.setContact(updatedContact);

        Shipping savedShipping = shippingRepository.save(shipping);

        return shippingRepository.findShippingResponseById(savedShipping.getId());
    }

    @Override
    public GenericResponse delete(UUID shippingId) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new NotFoundException("Shipping information with id " + shippingId + " not found"));

        shippingRepository.delete(shipping);

        return new GenericResponse(HttpStatus.OK.value(), "Shipping information deleted successfully");
    }

    private Shipping mapShippingRequestToShipping(ShippingRequest shippingRequest) {
        Shipping.ShippingBuilder builder = Shipping.builder()
                .firstName(shippingRequest.getFirstName())
                .lastName(shippingRequest.getLastName())
                .address1(shippingRequest.getAddress1())
                .country(shippingRequest.getCountry())
                .state(shippingRequest.getState())
                .city(shippingRequest.getCity())
                .zipCode(shippingRequest.getZipCode())
                .email(shippingRequest.getEmail())
                .contact(mapContactRequestToContact(shippingRequest.getContact()));

        if (shippingRequest.getAddress2() != null) {
            builder.address2(shippingRequest.getAddress2());
        }

        return builder.build();
    }

    private Contact mapContactRequestToContact(ContactRequest contactRequest) {
        return Contact.builder()
                .phoneNumber(contactRequest.getPhoneNumber())
                .country(contactRequest.getCountry())
                .iso2Code(contactRequest.getIso2Code())
                .dialCode(contactRequest.getDialCode())
                .build();
    }

    private AddressRequestDto mapShippingRequestToAddressRequestDto(ShippingRequest dto) {
        return new AddressRequestDto(
                dto.getAddress1(),
                dto.getCity(),
                dto.getState(),
                dto.getZipCode(),
                dto.getCountry(),
                null,
                dto.getContact().getPhoneNumber()
        );
    }
}
