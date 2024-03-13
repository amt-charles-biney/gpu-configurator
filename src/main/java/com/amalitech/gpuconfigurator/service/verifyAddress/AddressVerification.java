package com.amalitech.gpuconfigurator.service.verifyAddress;

import com.amalitech.gpuconfigurator.dto.shipping.AddressRequestDto;
import com.easypost.exception.EasyPostException;
import com.easypost.model.Address;
import jakarta.transaction.Transactional;

public interface AddressVerification {
    @Transactional
    Address verifyAddress(AddressRequestDto request) throws EasyPostException;
}
