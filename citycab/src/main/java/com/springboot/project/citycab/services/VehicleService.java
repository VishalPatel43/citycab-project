package com.springboot.project.citycab.services;

import com.springboot.project.citycab.entities.Vehicle;


public interface VehicleService {

    Vehicle findByNumberPlate(String numberPlate);

    Vehicle findByRegistrationNumber(String registrationNumber);

    Vehicle saveVehicle(Vehicle vehicle);
}
