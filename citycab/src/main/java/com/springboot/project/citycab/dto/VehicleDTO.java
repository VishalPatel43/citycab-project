package com.springboot.project.citycab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private Long vehicleId;

    // On specific format
    private String numberPlate;

    private String registrationNumber;

    private String ownerName;

    private String type; // e.g., SUV, Sedan, Hatchback

    private String model;

    private int capacity;

}
