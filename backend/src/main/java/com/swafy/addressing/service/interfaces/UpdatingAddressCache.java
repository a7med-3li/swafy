package com.swafy.addressing.service.interfaces;

import com.swafy.addressing.entity.Address;

public interface UpdatingAddressCache {
    //TODO: methods to store the results of GoogleMapsAPI into local DB
    void storeAddress(Address address);
}
