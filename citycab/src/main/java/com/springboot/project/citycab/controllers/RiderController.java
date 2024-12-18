package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/riders")
@RequiredArgsConstructor
//@Secured("ROLE_RIDER") // default role is "ROLE_RIDER" so put some condition here using @PreAuthorize
// If it's active as driver then not allowed to access this controller
// But ADMIN can access this controller
//@PreAuthorize("hasRole('RIDER') AND @userSecurity.hasActiveRole('RIDER') OR hasRole('ADMIN')")
public class RiderController {

    private final RiderService riderService;

    @PostMapping(path = "/requestRide")
    public ResponseEntity<RideRequestDTO> requestRide(
            @RequestBody RideRequestDTO rideRequestDTO) {
        return ResponseEntity.ok(riderService.requestRide(rideRequestDTO));
    }

    // cancel the rideRequest --> remove from the list of available drivers
    @PostMapping(path = "/cancelRideRequest/{rideRequestId}")
    public ResponseEntity<RideRequestDTO> cancelRideRequest(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(riderService.cancelRideRequestByRider(rideRequestId));
    }

    @PostMapping(path = "/cancelRide/{rideId}")
    public ResponseEntity<RideDTO> cancelRide(@RequestBody MessageDTO messageDTO,
                                              @PathVariable Long rideId) {
        return ResponseEntity.ok(riderService.cancelRide(rideId, messageDTO.getReason()));
    }

    @GetMapping(path = "/getAvailableDriversForRideRequest/{rideRequestId}")
    public ResponseEntity<List<DriverDTO>> getAvailableDriversForRideRequest(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(riderService.getAvailableDriversForRideRequest(rideRequestId));
    }

    @GetMapping(path = "/getMyProfile")
    public ResponseEntity<RiderDTO> getMyProfile() {
        return ResponseEntity.ok(riderService.getMyProfile());
    }

    @GetMapping(path = "/getMyRides")
    public ResponseEntity<Page<RideDTO>> getAllMyRides(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.DESC, "createdTime", "rideId"));
        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest));
    }

    @GetMapping("/getCancelledRides")
    public ResponseEntity<Page<CancelRideDTO>> getCancelledRides(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.ASC, "cancelRideId"));
        return ResponseEntity.ok(riderService.getCancelledRidesByRider(pageRequest));
    }

    @GetMapping("/getOtp/{rideId}")
    public ResponseEntity<OtpDTO> getOtp(@PathVariable Long rideId) {
        return ResponseEntity.ok(riderService.getOtp(rideId));
    }

    @PostMapping("/rateDriver/{rideId}")
    public ResponseEntity<DriverDTO> rateDriver(@PathVariable Long rideId,
                                                @RequestBody RatingDTO ratingDTO) {
        return ResponseEntity.ok(riderService.submitRating(rideId, ratingDTO));
    }

    @GetMapping("/getAllReviewsByRider")
    public ResponseEntity<Page<RatingDTO>> getReviewsByRider(
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.DESC, "ratingDate"));
        return ResponseEntity.ok(riderService.getReviewsByRider(pageRequest));
    }

    @GetMapping("/riderToDestinationDistanceTime/{rideId}")
    public ResponseEntity<DistanceTimeResponseDTO> riderToDestinationDistanceTime(
            @PathVariable Long rideId, @RequestBody PointDTO driverLocation) {
        return ResponseEntity.ok(riderService.riderToDestinationDistanceTime(rideId, driverLocation));
    }

    @GetMapping("/driverToRiderDistanceTime/{rideId}")
    public ResponseEntity<DistanceTimeResponseDTO> driverToRiderDistanceTime(
            @PathVariable Long rideId, @RequestBody PointDTO driverLocation) {
        return ResponseEntity.ok(riderService.driverToRiderDistanceTime(rideId, driverLocation));
    }
}
