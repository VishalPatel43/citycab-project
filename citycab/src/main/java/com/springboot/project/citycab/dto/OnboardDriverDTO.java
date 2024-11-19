package com.springboot.project.citycab.dto;

import lombok.Data;

@Data
public class OnboardDriverDTO {

    private Long aadharCardNumber;
    private String drivingLicenseNumber;

    private AddressDTO address;
    private VehicleDTO vehicle;

    private PointDTO currentLocation;

}
