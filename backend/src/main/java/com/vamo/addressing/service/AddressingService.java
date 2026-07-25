package com.vamo.addressing.service;

import com.vamo.addressing.dto.AutoCompleteResponse;
import com.vamo.addressing.repository.AddressingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AddressingService {
    private final AddressingRepository addressingRepository;
    //TODO: add the logic to provide an addressing layer before maps API call
    //TODO-Now: implement the logic to search for the address in googleMaps and store the results in the local DB

    public AutoCompleteResponse autoComplete(String address) {
        try {
            return new AutoCompleteResponse(
                    "internal",
                    addressingRepository.queryAddressesByAddressContaining(address)
            );
        } catch (Exception e) {
            return new AutoCompleteResponse("internal", Collections.emptyList());
        }
    }
}
