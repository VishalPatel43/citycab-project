package com.springboot.project.citycab.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.project.citycab.entities.enums.PaymentMethod;
import com.springboot.project.citycab.entities.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Ignore null fields
public class RideDTO {

    private Long rideId;

    private PointDTO pickupLocation;

    private PointDTO dropOffLocation;

    private LocalDateTime createdTime;

    private PaymentMethod paymentMethod;

    private RideStatus rideStatus;

    private Double fare;

    private OtpDTO otp;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private DriverDTO driver;

    private RiderDTO rider;

    private RatingDTO rating;

    private CancelRideDTO cancelRide;
}
