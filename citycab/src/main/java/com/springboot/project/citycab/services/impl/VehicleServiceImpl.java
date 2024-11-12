package com.springboot.project.citycab.services.impl;

import com.springboot.project.citycab.entities.Vehicle;
import com.springboot.project.citycab.repositories.VehicleRepository;
import com.springboot.project.citycab.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public Vehicle findByNumberPlate(String numberPlate) {
        return vehicleRepository.findByNumberPlate(numberPlate).orElse(null);
    }

    @Override
    public Vehicle findByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(registrationNumber).orElse(null);
    }

        @Override
        public Vehicle saveVehicle (Vehicle vehicle){
            return vehicleRepository.save(vehicle);
        }
    }
