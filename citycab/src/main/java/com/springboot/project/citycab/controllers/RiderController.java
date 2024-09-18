package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping(path = "/requestRide")
    public ResponseEntity<RideRequestDTO> requestRide(
            @RequestBody RideRequestDTO rideRequestDTO) {
        return ResponseEntity.ok(riderService.requestRide(rideRequestDTO));
    }
}
