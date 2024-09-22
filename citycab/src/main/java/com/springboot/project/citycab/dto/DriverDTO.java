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
    private Double rating;

    private Boolean available;
    private String vehicleId;
}
