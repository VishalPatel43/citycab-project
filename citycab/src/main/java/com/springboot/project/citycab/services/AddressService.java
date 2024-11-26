package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.AddressDTO;
import com.springboot.project.citycab.entities.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AddressService {

    Address findAddressById(Long addressId);

    Address saveAddress(Address address);

    AddressDTO saveAddress(Long addressId, AddressDTO addressDTO);

    AddressDTO getAddressById(Long addressId);

    Page<AddressDTO> getAllAddresses(PageRequest pageRequest);

}
