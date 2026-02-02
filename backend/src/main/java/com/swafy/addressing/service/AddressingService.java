package com.swafy.addressing.service;

import com.swafy.addressing.dto.AutoCompleteResponse;
import com.swafy.addressing.repository.AddressingRepository;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.BasicLogger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressingService {
    private final AddressingRepository addressingRepository;
    //TODO: add the logic to provide an addressing layer before maps API call
    //TODO-Now: implement the logic to return a list of addresses

    public AutoCompleteResponse autoComplete(String address){
        AutoCompleteResponse response = new AutoCompleteResponse();
        response.setSource("internal");
        try {
            response.setAddresseList(
                    addressingRepository.queryAddressesByAddressContaining(address)
            );
        } catch (Exception e) {
            response.setAddresseList(Collections.emptyList());
        }
        response.setAddresseList(addressingRepository.queryAddressesByAddressContaining(address));
        return response;
    }
}
