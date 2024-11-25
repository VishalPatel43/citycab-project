package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.entities.*;
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

import java.util.List;
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
    private final CancelRideService cancelRideService;

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

        userService.validateUserForRole(userId, Role.ADMIN, "Admin");
        User user = userService.validateUserForRole(userId, Role.DRIVER, "Driver");

        Address address = addressService.saveAddress(onboardDriverDTO.getAddress()); // we get the address id after saving

        // Map and save or retrieve the Vehicle (if vehicle already exists)
        Vehicle vehicle = vehicleService.validateAndCreateVehicle(onboardDriverDTO.getVehicle()); // we get the vehicle id after saving

        Point currentLocation = modelMapper.map(onboardDriverDTO.getCurrentLocation(), Point.class);

        Driver newDriver = driverService.createDriver(user, address, vehicle,
                onboardDriverDTO, currentLocation);

        return driverService.mapDriverToDTO(newDriver);
    }

    @Transactional
    @Override
    public DriverDTO onboardNewVehicle(Long driverId, VehicleDTO vehicleDTO) {

        Vehicle vehicle = vehicleService.validateExistingVehicle(vehicleDTO);
        Driver driver = driverService.getDriverById(driverId);

        // Add the vehicle to the driver
        driver.getVehicles().add(vehicle);
        driver = driverService.saveDriver(driver);

        return driverService.mapDriverToDTO(driver);
    }

    @Transactional
    @Override
    public DriverDTO assignDriverToVehicle(Long driverId, VehicleDTO vehicleDTO) {

        Vehicle vehicle = vehicleService.validateExistingVehicle(vehicleDTO);

        Driver driver = driverService.getDriverById(driverId);

        // Add the vehicle to the driver
        driver.getVehicles().add(vehicle);
        driver = driverService.saveDriver(driver);

        return driverService.mapDriverToDTO(driver);

    }

    @Transactional
    @Override
    public DriverDTO deAssignDriverToVehicle(Long driverId, VehicleDTO vehicleDTO) {

        Vehicle vehicle = vehicleService.validateExistingVehicle(vehicleDTO);

        Driver driver = driverService.getDriverById(driverId);

        // Add the vehicle to the driver
        driver.getVehicles().remove(vehicle);
        driver = driverService.saveDriver(driver);

        return driverService.mapDriverToDTO(driver);

    }

    @Transactional
    @Override
    public DriverDTO updateDriverAddress(Long driverId, AddressDTO addressDTO) {
        Driver driver = driverService.getDriverById(driverId);
        Address existingAddress = driver.getAddress();

        modelMapper.map(addressDTO, existingAddress);

        addressService.saveAddress(existingAddress);

        // for DTO
        driver.setAddress(existingAddress); // we get updated address in the driver DTO

        return driverService.mapDriverToDTO(driver);
    }

    @Transactional
    @Override
    public DeleteDTO removeVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);

        vehicle.getDrivers().forEach(driver -> driver.getVehicles().remove(vehicle));
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

    @Override
    public Page<CancelRideDTO> getCancelledRidesByRider(PageRequest pageRequest) {
        return getCancelledRides(Role.RIDER, pageRequest);
    }

    @Override
    public Page<CancelRideDTO> getCancelledRidesByDriver(PageRequest pageRequest) {
        return getCancelledRides(Role.DRIVER, pageRequest);
    }

    private Page<CancelRideDTO> getCancelledRides(Role role, PageRequest pageRequest) {
        Page<CancelRide> cancelledRides = cancelRideService.getCancelRideByRole(role, pageRequest);

        return cancelledRides.map(cancelRide -> {
            CancelRideDTO cancelRideDTO = modelMapper.map(cancelRide, CancelRideDTO.class);
            cancelRideDTO.getRide().getDriver().setVehicles(null);
            return cancelRideDTO;
        });
    }

}
