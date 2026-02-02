package com.swafy.addressing.repository;

import com.swafy.addressing.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressingRepository extends JpaRepository<Address, Long> {
    List<Address> queryAddressesByAddressContaining(String address);
}
