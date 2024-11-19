package com.springboot.project.citycab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDTO {

    private Long driverId;
    private UserDTO user;
    private Double avgRating;
    private Boolean available;
    private Long aadharCardNumber;
    private String drivingLicenseNumber;

    private AddressDTO address;
    private VehicleDTO vehicle;

}
