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
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
        userService.saveUser(user);

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
        Address savedAddress = addressService.saveAddress(address); // we get the address id after saving

        // Map and save or retrieve the Vehicle (if vehicle already exists)
        VehicleDTO vehicleDTO = onboardDriverDTO.getVehicle();
        Vehicle existingVehicle = vehicleService.findByRegistrationNumber(vehicleDTO.getRegistrationNumber());

        Vehicle vehicle = existingVehicle;
        if (existingVehicle == null) {
            vehicle = modelMapper.map(vehicleDTO, Vehicle.class);
            vehicle = vehicleService.saveVehicle(vehicle); // we get the vehicle id after saving
        }

        log.info("Vehicle1: {}", vehicle);

        Point currentLocation = modelMapper.map(onboardDriverDTO.getCurrentLocation(), Point.class);

        Long aadharCardNumber = onboardDriverDTO.getAadharCardNumber();
        if (driverService.findDriverByAadharCardNumber(aadharCardNumber) != null)
            throw new RuntimeConflictException("Driver with Aadhar Card Number: " + aadharCardNumber + " already exists");

        String drivingLicenseNumber = onboardDriverDTO.getDrivingLicenseNumber();
        if (driverService.findDriverByDrivingLicenseNumber(drivingLicenseNumber) != null)
            throw new RuntimeConflictException("Driver with Driving License Number: " + drivingLicenseNumber + " already exists");

        Driver createDriver = Driver.builder()
                .user(user)
                .avgRating(0.0)
                .available(true)
                .address(savedAddress)
//                .vehicles(Set.of(vehicle))
                .vehicles(new HashSet<>(Set.of(vehicle))) // Convert to mutable set
                .currentLocation(currentLocation)  // --> set the current location
                .aadharCardNumber(aadharCardNumber)
                .drivingLicenseNumber(drivingLicenseNumber)
                .build();

        user.getRoles().add(Role.DRIVER);
        userService.saveUser(user);

        Driver savedDriver = driverService.saveDriver(createDriver);
        log.info("Driver1: {}", savedDriver);

//        Set<Driver> drivers = vehicle.getDrivers();
//        log.info("Drivers: {}", drivers);
//        if (drivers == null || drivers.isEmpty()) {
//            drivers = new HashSet<>();
//            vehicle.setDrivers(drivers);
//            log.info("Vehicle2: {}", vehicle);
//        }
//        drivers.add(savedDriver);
//
//        log.info("Vehicle3: {}", vehicle);
//        Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);

//        savedDriver.getVehicles().add(savedVehicle); // for DTO result, not necessary for DB
        // already saved in the vehicleService.saveVehicle(vehicle) method

        DriverDTO driverDTO = modelMapper.map(savedDriver, DriverDTO.class);
        Set<VehicleDTO> vehicleDTOS = savedDriver.getVehicles().stream()
                .map(v -> modelMapper.map(v, VehicleDTO.class))
                .collect(Collectors.toSet());

        driverDTO.setVehicles(vehicleDTOS);
//        driverDTO.setVehicle(modelMapper.map(savedVehicle, VehicleDTO.class));

        return driverDTO;
    }

    @Transactional
    @Override
    public DriverDTO onboardNewVehicle(Long driverId, VehicleDTO vehicleDTO) {

//        User currentUser = userService.getCurrentUser();
//        if (!currentUser.getRoles().contains(Role.ADMIN))
//            throw new RuntimeException("You are not authorized to onboard a new vehicle");

        Vehicle vehicle = vehicleService.findByRegistrationNumber(vehicleDTO.getRegistrationNumber());
        if (vehicle != null)
            throw new RuntimeConflictException("Vehicle with registration number: " + vehicleDTO.getRegistrationNumber() + " already exists");

        vehicle = vehicleService.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (vehicle != null)
            throw new RuntimeConflictException("Vehicle with number plate: " + vehicleDTO.getNumberPlate() + " already exists");

        Driver driver = driverService.getDriverById(driverId);
        log.info("Driver: {}", driver);
        log.info("Driver Vehicles: {}", driver.getVehicles());

        vehicle = modelMapper.map(vehicleDTO, Vehicle.class);
        vehicle = vehicleService.saveVehicle(vehicle);

        // Add the vehicle to the driver
        driver.getVehicles().add(vehicle);
        driver = driverService.saveDriver(driver);

        log.info("Vehicle Drivers: {}", vehicle.getDrivers());
//        vehicle.getDrivers().add(driver);
//        vehicle.setDrivers(new HashSet<>(Set.of(driver))); // Convert to mutable set


//        vehicle.setDrivers(new HashSet<>()); // Convert to mutable set
//        vehicle.getDrivers().add(driver);
//        vehicleService.saveVehicle(vehicle);

        DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
        Set<VehicleDTO> vehicleDTOS = driver.getVehicles().stream()
                .map(v -> modelMapper.map(v, VehicleDTO.class))
                .collect(Collectors.toSet());
        driverDTO.setVehicles(vehicleDTOS);

        return driverDTO;
    }

    @Transactional
    @Override
    public DriverDTO assignDriverToVehicle(Long driverId, VehicleDTO vehicleDTO) {

//        User currentUser = userService.getCurrentUser();
//        if (!currentUser.getRoles().contains(Role.ADMIN))
//            throw new RuntimeException("You are not authorized to onboard a new vehicle");

        Vehicle vehicleServiceByRegistrationNumber = vehicleService.findByRegistrationNumber(vehicleDTO.getRegistrationNumber());
        if (vehicleServiceByRegistrationNumber == null)
            throw new RuntimeConflictException("Vehicle with registration number: " + vehicleDTO.getRegistrationNumber() + " does not exist");

        Vehicle vehicleServiceByNumberPlate = vehicleService.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (vehicleServiceByNumberPlate == null)
            throw new RuntimeConflictException("Vehicle with number plate: " + vehicleDTO.getNumberPlate() + " does not exist");

        if (!vehicleServiceByNumberPlate.getVehicleId().equals(vehicleServiceByRegistrationNumber.getVehicleId()))
            throw new RuntimeConflictException("Vehicle with registration number: " + vehicleDTO.getRegistrationNumber() + " and number plate: " + vehicleDTO.getNumberPlate() + " do not match");

        Driver driver = driverService.getDriverById(driverId);

        // Copy the vehicle object
        Vehicle vehicle = modelMapper.map(vehicleServiceByRegistrationNumber, Vehicle.class);
        // Add the vehicle to the driver
        driver.getVehicles().add(vehicle);
        driver = driverService.saveDriver(driver);

//        vehicle.getDrivers().add(driver);
//        vehicle.setDrivers(new HashSet<>(Set.of(driver))); // Convert to mutable set

//        vehicle.setDrivers(new HashSet<>()); // Convert to mutable set
//        vehicle.getDrivers().add(driver);
//        vehicleService.saveVehicle(vehicle);

        DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
        Set<VehicleDTO> vehicleDTOS = driver.getVehicles().stream()
                .map(v -> modelMapper.map(v, VehicleDTO.class))
                .collect(Collectors.toSet());
        driverDTO.setVehicles(vehicleDTOS);

        return driverDTO;

    }

    @Transactional
    @Override
    public DriverDTO deAssignDriverToVehicle(Long driverId, VehicleDTO vehicleDTO) {

//        User currentUser = userService.getCurrentUser();
//        if (!currentUser.getRoles().contains(Role.ADMIN))
//            throw new RuntimeException("You are not authorized to onboard a new vehicle");

        Vehicle vehicleServiceByRegistrationNumber = vehicleService.findByRegistrationNumber(vehicleDTO.getRegistrationNumber());
        if (vehicleServiceByRegistrationNumber == null)
            throw new RuntimeConflictException("Vehicle with registration number: " + vehicleDTO.getRegistrationNumber() + " does not exist");

        Vehicle vehicleServiceByNumberPlate = vehicleService.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (vehicleServiceByNumberPlate == null)
            throw new RuntimeConflictException("Vehicle with number plate: " + vehicleDTO.getNumberPlate() + " does not exist");

        if (!vehicleServiceByNumberPlate.getVehicleId().equals(vehicleServiceByRegistrationNumber.getVehicleId()))
            throw new RuntimeConflictException("Vehicle with registration number: " + vehicleDTO.getRegistrationNumber() + " and number plate: " + vehicleDTO.getNumberPlate() + " do not match");

        Driver driver = driverService.getDriverById(driverId);

        // Copy the vehicle object
        Vehicle vehicle = modelMapper.map(vehicleServiceByRegistrationNumber, Vehicle.class);
        // Add the vehicle to the driver
        driver.getVehicles().remove(vehicle);
        driver = driverService.saveDriver(driver);

//        vehicle.getDrivers().add(driver);
//        vehicle.setDrivers(new HashSet<>(Set.of(driver))); // Convert to mutable set

//        vehicle.setDrivers(new HashSet<>()); // Convert to mutable set
//        vehicle.getDrivers().add(driver);
//        vehicleService.saveVehicle(vehicle);

        DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
        Set<VehicleDTO> vehicleDTOS = driver.getVehicles().stream()
                .map(v -> modelMapper.map(v, VehicleDTO.class))
                .collect(Collectors.toSet());
        driverDTO.setVehicles(vehicleDTOS);

        return driverDTO;

    }

    @Transactional
    @Override
    public DriverDTO updateDriverAddress(Long driverId, AddressDTO addressDTO) {
        Driver driver = driverService.getDriverById(driverId);
//        User currentUser = userService.getCurrentUser();

//        if (!currentUser.getRoles().contains(Role.ADMIN))
//            throw new RuntimeException("You are not authorized to update profile for this driver");

        Long addressId = driver.getAddress().getAddressId();
        Address existingAddress = addressService.findAddressById(addressId);
        log.info("Existing Address: {}", existingAddress);
        modelMapper.map(addressDTO, existingAddress);
        log.info("Updated Address: {}", existingAddress);

        existingAddress.setAddressId(addressId);
        addressService.saveAddress(existingAddress);

        // for DTO
        driver.setAddress(existingAddress);

        DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);

        Set<VehicleDTO> vehicleDTOS = driver.getVehicles().stream()
                .map(v -> modelMapper.map(v, VehicleDTO.class))
                .collect(Collectors.toSet());
        driverDTO.setVehicles(vehicleDTOS);

        return modelMapper.map(driver, DriverDTO.class);
    }

    @Transactional
    @Override
    public DeleteDTO removeVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
//        User currentUser = userService.getCurrentUser();
//
//        if (!currentUser.getRoles().contains(Role.ADMIN))
//            throw new RuntimeException("You are not authorized to remove this vehicle");

        // Remove the vehicle from all drivers
//        vehicle.getDrivers().forEach(driver -> {
//                    driver.getVehicles().remove(vehicle);
//                    driverService.saveDriver(driver);
//                }
//        );
//        vehicleService.deleteVehicle(vehicle);

        vehicle.getDrivers().forEach(driver -> {
            driver.getVehicles().remove(vehicle);
        });
//        vehicleService.saveVehicle(vehicle);
        vehicleService.deleteVehicle(vehicle); // delete from the vehicle table

        return new DeleteDTO("Vehicle with id: " + vehicleId + " removed successfully");
    }

    @Override
    public List<DriverDTO> getDriversByVehicleId(Long vehicleId) {
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
        return vehicle.getDrivers().stream()
                .map(driver -> {
                    DriverDTO driverDTO = modelMapper.map(driver, DriverDTO.class);
                    driverDTO.setVehicles(null);
                    driverDTO.setAddress(null);
//                    driverDTO.setUser(null);
                    return driverDTO;
                })
                .collect(Collectors.toList());
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
