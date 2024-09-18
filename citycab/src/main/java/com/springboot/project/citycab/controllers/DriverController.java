package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.RideDTO;
import com.springboot.project.citycab.dto.RideStartDTO;
import com.springboot.project.citycab.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping(path = "/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }

    @PostMapping(path = "/startRide/{rideRequestId}")
    public ResponseEntity<RideDTO> startRide(@PathVariable Long rideRequestId,
                                             @RequestBody RideStartDTO rideStartDTO) {
        return ResponseEntity.ok(driverService.startRide(rideRequestId, rideStartDTO.getOtp()));
    }

}
