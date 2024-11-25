package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.AddressDTO;
import com.springboot.project.citycab.entities.Address;

public interface AddressService {

   Address saveAddress(Address address);

   Address saveAddress(AddressDTO addressDTO);

    Address findAddressById(Long addressId);
}
