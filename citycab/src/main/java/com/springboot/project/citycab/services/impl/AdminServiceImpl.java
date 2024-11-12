package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.Address;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.User;
import com.springboot.project.citycab.entities.Vehicle;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DriverService driverService;
    private final RiderService riderService;
    private final UserService userService;
    private final AddressService addressService;
    private final VehicleService vehicleService;

    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public UserDTO onBoardNewAdmin(Long userId) {
        User user = userService.getUserById(userId);

        if (user.getRoles().contains(Role.ADMIN))
            throw new RuntimeConflictException("User with id: " + userId + " is already an Admin");

        user.getRoles().add(Role.ADMIN);
        userService.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    @Override
    public DriverDTO onboardNewDriver(Long userId, OnboardDriverDTO onboardDriverDTO) {
        User user = userService.getUserById(userId);

        if (user.getRoles().contains(Role.ADMIN))
            throw new RuntimeConflictException("User with id: " + userId + " is an Admin");

        if (user.getRoles().contains(Role.DRIVER))
            throw new RuntimeConflictException("User with id: " + userId + " is already a Driver");

        Address address = modelMapper.map(onboardDriverDTO.getAddress(), Address.class);
        Address savedAddress = addressService.saveAddress(address);

        // Map and save or retrieve the Vehicle (if vehicle already exists)
        VehicleDTO vehicleDTO = onboardDriverDTO.getVehicle();
        Vehicle existingVehicle = vehicleService.findByRegistrationNumber(vehicleDTO.getRegistrationNumber());

        Vehicle vehicle = existingVehicle;
        if (existingVehicle == null) {
            vehicle = modelMapper.map(vehicleDTO, Vehicle.class);
            vehicle = vehicleService.saveVehicle(vehicle);
        }

        Driver createDriver = Driver.builder()
                .user(user)
                .avgRating(0.0)
                .available(true)
                .address(savedAddress)
                .vehicles(Set.of(vehicle))
                .build();

        user.getRoles().add(Role.DRIVER);
        userService.save(user);

        Driver savedDriver = driverService.saveDriver(createDriver);

        // Add the driver to the vehicle's driver set if not already associated
        vehicle.getDrivers().add(savedDriver);
        vehicleService.saveVehicle(vehicle);

        return modelMapper.map(savedDriver, DriverDTO.class);
    }

    @Override
    public DriverDTO onboardNewVehicle(Long driverId, VehicleDTO vehicleDTO) {

        Vehicle vehicle = vehicleService.findByRegistrationNumber(vehicleDTO.getRegistrationNumber());
        if (vehicle != null)
            throw new RuntimeConflictException("Vehicle with registration number: " + vehicleDTO.getRegistrationNumber() + " already exists");

        Driver driver = driverService.getDriverById(driverId);

        vehicle = modelMapper.map(vehicleDTO, Vehicle.class);
        vehicle = vehicleService.saveVehicle(vehicle);

        // Add the vehicle to the driver
        driver.getVehicles().add(vehicle);
        driver = driverService.saveDriver(driver);

        vehicle.getDrivers().add(driver);
        vehicleService.saveVehicle(vehicle);

        return modelMapper.map(driver, DriverDTO.class);
    }

    @Override
    public Page<RiderDTO> findRidersByName(String name, PageRequest pageRequest) {
        return riderService.findRidersByName(name, pageRequest);
    }

    @Override
    public Page<DriverDTO> findDriversByName(String name, PageRequest pageRequest) {
        return driverService.findDriversByName(name, pageRequest);
    }


}
