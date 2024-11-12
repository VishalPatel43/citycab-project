package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/drivers")
@RequiredArgsConstructor
//@Secured("ROLE_DRIVER")
public class DriverController {

    private final DriverService driverService;

    @PostMapping(path = "/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }

    @PostMapping(path = "/startRide/{rideId}")
    public ResponseEntity<RideDTO> startRide(@PathVariable Long rideId,
                                             @RequestBody OtpDTO rideStartDTO) {
        return ResponseEntity.ok(driverService.startRide(rideId, rideStartDTO.getOtp()));
    }

    @PostMapping(path = "/endRide/{rideId}")
    public ResponseEntity<RideDTO> endRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.endRide(rideId));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDTO> cancelRide(@RequestBody MessageDTO messageDTO,
                                              @PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.cancelRide(rideId, messageDTO.getReason()));
    }

    @GetMapping("/getAvailableRideRequests")
    public ResponseEntity<List<RideRequestDTO>> getAvailableRideRequests() {
        return ResponseEntity.ok(driverService.getAvailableRideRequests());
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDTO> getMyProfile() {
        return ResponseEntity.ok(driverService.getMyProfile());
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<Page<RideDTO>> getAllMyRides(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.DESC, "createdTime", "rideId"));
        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest));
    }

    @GetMapping("/getCancelledRides")
    public ResponseEntity<Page<CancelRideDTO>> getCancelledRides(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.ASC, "cancelRideId"));
        return ResponseEntity.ok(driverService.getCancelledRidesByDriver(pageRequest));
    }

    @GetMapping("/getReceivedReviews")
    public ResponseEntity<Page<RatingDTO>> getReviewsForDriver(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.ASC, "ratingId"));
        return ResponseEntity.ok(driverService.getReviewsForDriver(pageRequest));
    }
}
