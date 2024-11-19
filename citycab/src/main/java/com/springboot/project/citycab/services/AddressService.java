package com.springboot.project.citycab.services;

import com.springboot.project.citycab.entities.Address;

public interface AddressService {

   Address saveAddress(Address address);

    Address findAddressById(Long addressId);
}
