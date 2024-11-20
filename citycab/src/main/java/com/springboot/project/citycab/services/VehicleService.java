package com.springboot.project.citycab.services;

import com.springboot.project.citycab.entities.Vehicle;


public interface VehicleService {

    Vehicle findVehicleById(Long vehicleId);

    Vehicle findByNumberPlate(String numberPlate);

    Vehicle findByRegistrationNumber(String registrationNumber);

    Vehicle saveVehicle(Vehicle vehicle);

    void deleteVehicle(Vehicle vehicle);

    void updateVehicleAvailability(Vehicle vehicle, boolean available);
}
