package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.constants.enums.PaymentMethod;
import com.springboot.project.citycab.constants.enums.RideRequestStatus;
import com.springboot.project.citycab.entities.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDTO {

    private Long rideRequestId;

    private PointDTO pickupLocation;

    private PointDTO dropOffLocation;

    private LocalDateTime requestedTime;

    private PaymentMethod paymentMethod;

    private Double rideDistance; // We calculate the distance between the pickup and drop-off locations

    private Double rideTime; // We calculate the time between the pickup and drop-off locations

    private Double fare; // We calculate the fare based on the distance between the pickup and drop-off locations

    private RideRequestStatus rideRequestStatus; // The first we set to the PENDING status

    private RiderDTO rider;

    List<Driver> drivers;

}
