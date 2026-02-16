package com.swafy.addressing.service;

import com.swafy.addressing.entity.Address;
import com.swafy.addressing.repository.AddressingRepository;
import com.swafy.addressing.service.interfaces.UpdatingAddressCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAddressCache implements UpdatingAddressCache {

    //Note: this implementation supposes that the user will not be able to hit "Search" unless there is no matching addresses

    private final AddressingRepository addressingRepository;
    @Override
    public void storeAddress(Address address) {
        addressingRepository.save(address);
        log.info("The address cache has been updated with address: " + address.getAddress());
    }
}
