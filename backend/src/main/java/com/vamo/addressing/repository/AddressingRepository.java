package com.vamo.addressing.repository;

import com.vamo.addressing.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressingRepository extends JpaRepository<Address, Long> {
    List<Address> queryAddressesByAddressContaining(String address);
}
