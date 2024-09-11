package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.entities.enums.PaymentMethod;
import com.springboot.project.citycab.entities.enums.RideRequestStatus;
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

    private Double fare;

    private RideRequestStatus rideRequestStatus;

    private RiderDTO rider;


}
