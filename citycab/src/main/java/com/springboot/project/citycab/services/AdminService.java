package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AdminService {


    UserDTO onBoardNewAdmin(Long userId);

    DriverDTO onboardNewDriver(Long userId, OnboardDriverDTO onboardDriverDTO);

    DriverDTO onboardNewVehicle(Long driverId, VehicleDTO vehicleDTO);

    DriverDTO assignDriverToVehicle(Long driverId, VehicleDTO vehicleDTO);

    DriverDTO updateDriverAddress(Long driverId, AddressDTO addressDTO);

    DriverDTO updateDriverVehicle(Long driverId, VehicleDTO vehicleDTO);

    String removeVehicle(Long vehicleId);

    Page<RiderDTO> findRidersByName(String name, PageRequest pageRequest);

    Page<DriverDTO> findDriversByName(String name, PageRequest pageRequest);

}
