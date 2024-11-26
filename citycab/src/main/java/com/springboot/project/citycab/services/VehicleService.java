package com.springboot.project.citycab.services;

import com.springboot.project.citycab.dto.VehicleDTO;
import com.springboot.project.citycab.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface VehicleService {

    Vehicle findVehicleById(Long vehicleId);

    Vehicle findByNumberPlate(String numberPlate);

    Vehicle findByRegistrationNumber(String registrationNumber);

    Vehicle saveVehicle(Vehicle vehicle);

    void deleteVehicle(Vehicle vehicle);

    void updateVehicleAvailability(Vehicle vehicle, boolean available);

    Vehicle validateAndCreateVehicle(VehicleDTO vehicleDTO);

    Vehicle validateExistingVehicle(VehicleDTO vehicleDTO);

    VehicleDTO updateVehicle(Long vehicleId, VehicleDTO vehicleDTO);

    VehicleDTO getVehicleById(Long vehicleId);

    Page<VehicleDTO> getAllVehicles(PageRequest pageRequest);

}
