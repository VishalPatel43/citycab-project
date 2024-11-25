package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.AddressDTO;
import com.springboot.project.citycab.entities.Address;
import com.springboot.project.citycab.repositories.AddressRepository;
import com.springboot.project.citycab.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    @Override
    public Address saveAddress(AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);
        return saveAddress(address);
    }

    @Override
    public Address findAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));
    }
}
