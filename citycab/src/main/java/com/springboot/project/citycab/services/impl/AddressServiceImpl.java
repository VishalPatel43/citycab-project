package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.AddressDTO;
import com.springboot.project.citycab.entities.Address;
import com.springboot.project.citycab.repositories.AddressRepository;
import com.springboot.project.citycab.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Override
    public Address findAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with id: " + addressId));
    }

    @Transactional
    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    @Override
    public AddressDTO saveAddress(Long addressId, AddressDTO addressDTO) {
        findAddressById(addressId);
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setAddressId(addressId);
        return modelMapper.map(saveAddress(address), AddressDTO.class);
    }


    @Override
    public AddressDTO getAddressById(Long addressId) {
        return modelMapper.map(findAddressById(addressId), AddressDTO.class);
    }

    @Override
    public Page<AddressDTO> getAllAddresses(PageRequest pageRequest) {
        return addressRepository.findAll(pageRequest)
                .map(address -> modelMapper.map(address, AddressDTO.class));
    }

}
