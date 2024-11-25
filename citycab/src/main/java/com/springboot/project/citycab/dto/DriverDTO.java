package com.springboot.project.citycab.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDTO {

    private Long driverId;
    private UserDTO user;
    private Double avgRating;
    private Boolean available;
    private Long aadhaarCardNumber;
    private String drivingLicenseNumber;

    private PointDTO currentLocation;

    private VehicleDTO currentVehicle;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AddressDTO address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<VehicleDTO> vehicles;

}
