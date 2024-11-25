package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.dto.VehicleDTO;
import com.springboot.project.citycab.entities.Vehicle;
import com.springboot.project.citycab.exceptions.RuntimeConflictException;
import com.springboot.project.citycab.repositories.VehicleRepository;
import com.springboot.project.citycab.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    private final ModelMapper modelMapper;

    @Override
    public Vehicle findVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle with id: " + vehicleId + " not found"));
    }

    @Override
    public Vehicle findByNumberPlate(String numberPlate) {
        return vehicleRepository.findByNumberPlate(numberPlate).orElse(null);
    }

    @Override
    public Vehicle findByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(registrationNumber).orElse(null);
    }

    @Transactional
    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    @Override
    public void deleteVehicle(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }

    @Transactional
    @Override
    public void updateVehicleAvailability(Vehicle vehicle, boolean available) {
        vehicle.setAvailable(available);
        vehicleRepository.save(vehicle);
    }

    @Transactional
    @Override
    public Vehicle validateAndCreateVehicle(VehicleDTO vehicleDTO) {
        if (findByRegistrationNumber(vehicleDTO.getRegistrationNumber()) != null ||
                findByNumberPlate(vehicleDTO.getNumberPlate()) != null) {
            throw new RuntimeConflictException("Vehicle already exists with the given details");
        }
        return saveVehicle(modelMapper.map(vehicleDTO, Vehicle.class));
    }

    @Override
    public Vehicle validateExistingVehicle(VehicleDTO vehicleDTO) {
        // check both registration number and number plate
        Vehicle vehicle = findByRegistrationNumber(vehicleDTO.getRegistrationNumber());

        if (vehicle == null || !vehicle.getNumberPlate().equals(vehicleDTO.getNumberPlate()))
            throw new RuntimeConflictException("Vehicle with registration number: " + vehicleDTO.getRegistrationNumber() + " does not exist");

        return vehicle;
    }
}
