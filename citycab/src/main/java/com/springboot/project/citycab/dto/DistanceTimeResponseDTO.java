package com.springboot.project.citycab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DistanceTimeResponseDTO {
    private double distanceKm; // Distance in kilometers
    private double timeMinutes; // Time in minutes
}
