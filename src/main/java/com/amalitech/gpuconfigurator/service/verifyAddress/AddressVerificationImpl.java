package com.amalitech.gpuconfigurator.service.verifyAddress;

import com.amalitech.gpuconfigurator.dto.shipping.AddressRequestDto;
import com.easypost.exception.EasyPostException;
import com.easypost.exception.General.MissingParameterError;
import com.easypost.model.Address;
import com.easypost.service.EasyPostClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AddressVerificationImpl implements AddressVerification {
    @Value("${easy-test-key}")
    private String easyPost;
    @Override
    public Address verifyAddress(AddressRequestDto request) throws EasyPostException {
        EasyPostClient client = new EasyPostClient(easyPost);

        HashMap<String, Object> params = new HashMap<>();

        params.put("street1", request.street1());
        params.put("city", request.city());
        params.put("state", request.state());
        params.put("zip", request.zip());
        params.put("country", request.country());
        params.put("company", request.company() != null ? request.company() :"");
        params.put("phone", request.phone());
        params.put("verify_strict", false); // set to true in product mode

        Address address = client.address.create(params);

        return client.address.verify(address.getId());
    }
}
