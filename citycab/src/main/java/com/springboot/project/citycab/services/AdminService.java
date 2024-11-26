package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface AdminService {


    UserDTO onBoardNewAdmin(Long userId);

    DriverDTO onboardNewDriver(Long userId, OnboardDriverDTO onboardDriverDTO);

    DriverDTO onboardNewVehicle(Long driverId, VehicleDTO vehicleDTO);

    DriverDTO assignDriverToVehicle(Long driverId, VehicleDTO vehicleDTO);

    DriverDTO deAssignDriverToVehicle(Long driverId, VehicleDTO vehicleDTO);

    DriverDTO updateDriverAddress(Long driverId, AddressDTO addressDTO);

    DeleteDTO removeVehicle(Long vehicleId);

    List<DriverDTO> getDriversByVehicleId(Long vehicleId);

    Page<RiderDTO> findRidersByName(String name, PageRequest pageRequest);

    Page<DriverDTO> findDriversByName(String name, PageRequest pageRequest);

    Page<CancelRideDTO> getCancelledRidesByRider(PageRequest pageRequest);

    Page<CancelRideDTO> getCancelledRidesByDriver(PageRequest pageRequest);


}
