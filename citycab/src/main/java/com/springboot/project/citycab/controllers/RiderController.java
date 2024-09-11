package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.RideRequestDTO;
import com.springboot.project.citycab.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/rider")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @GetMapping(path = "/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Rider Controller works");
    }

    @PostMapping(path = "/requestRide")
    public ResponseEntity<RideRequestDTO> requestRide(
            @RequestBody RideRequestDTO rideRequestDTO) {
        return ResponseEntity.ok(riderService.requestRide(rideRequestDTO));
    }
}
