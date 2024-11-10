package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.constants.enums.PaymentMethod;
import com.springboot.project.citycab.constants.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDTO {

    private Long rideRequestId;

    private PointDTO pickupLocation;

    private PointDTO dropOffLocation;

    private LocalDateTime requestedTime;

    private PaymentMethod paymentMethod;

    private Double fare; // We calculate the fare based on the distance between the pickup and drop-off locations

    private RideRequestStatus rideRequestStatus; // The first we set to the PENDING status

    private RiderDTO rider;


}
