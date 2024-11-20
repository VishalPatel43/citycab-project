package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.*;
import com.springboot.project.citycab.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/getRidersByName")
    public ResponseEntity<Page<RiderDTO>> getRidersByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.ASC, "riderId"));

        return ResponseEntity.ok(adminService.findRidersByName(name, pageRequest));
    }

    @GetMapping("/getDriversByName")
    public ResponseEntity<Page<DriverDTO>> getDriversByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") Integer pageOffset,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.ASC, "driverId"));

        return ResponseEntity.ok(adminService.findDriversByName(name, pageRequest));
    }

    // On Board ADMIN --> If we make admin then add driver also
    //    @Secured("ROLE_ADMIN")
    @PostMapping(path = "/onBoardNewAdmin/{userId}")
    ResponseEntity<UserDTO> onBoardNewAdmin(@PathVariable Long userId) {
        return new ResponseEntity<>(adminService.onBoardNewAdmin(userId), HttpStatus.CREATED);
    }

    // On Board DRIVER
    //    @Secured("ROLE_ADMIN")
    @PostMapping(path = "/onBoardNewDriver/{userId}")
    ResponseEntity<DriverDTO> onBoardNewDriver(@PathVariable Long userId,
                                               @RequestBody OnboardDriverDTO onboardDriverDTO) {
        return new ResponseEntity<>(adminService.onboardNewDriver(userId,
                onboardDriverDTO), HttpStatus.CREATED);
    }

    // On Board VEHICLE
    //    @Secured("ROLE_ADMIN")
    @PostMapping(path = "/onBoardNewVehicle/{driverId}")
    ResponseEntity<DriverDTO> onBoardNewVehicle(@PathVariable Long driverId,
                                                @RequestBody VehicleDTO vehicleDTO) {
        return new ResponseEntity<>(adminService.onboardNewVehicle(driverId,
                vehicleDTO), HttpStatus.CREATED);
    }

    @PostMapping(path = "/assignDriverToVehicle/{driverId}")
    ResponseEntity<DriverDTO> assignDriverToVehicle(@PathVariable Long driverId,
                                                    @RequestBody VehicleDTO vehicleDTO) {
        return new ResponseEntity<>(adminService.assignDriverToVehicle(driverId, vehicleDTO), HttpStatus.OK);
    }

    @PostMapping(path = "/updateDriverAddress/{driverId}")
    ResponseEntity<DriverDTO> updateDriverAddress(@PathVariable Long driverId,
                                                  @RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(adminService.updateDriverAddress(driverId, addressDTO), HttpStatus.OK);
    }

    @PostMapping(path = "/deAssignDriverToVehicle/{driverId}")
    ResponseEntity<DriverDTO> deAssignDriverToVehicle(@PathVariable Long driverId,
                                                      @RequestBody VehicleDTO vehicleDTO) {
        return new ResponseEntity<>(adminService.deAssignDriverToVehicle(driverId, vehicleDTO), HttpStatus.OK);
    }

    @PostMapping(path = "/removeVehicle/{vehicleId}")
    ResponseEntity<DeleteDTO> removeVehicle(@PathVariable Long vehicleId) {
        return new ResponseEntity<>(adminService.removeVehicle(vehicleId), HttpStatus.OK);
    }

    @GetMapping(path = "/getDriversByVehicleId/{vehicleId}")
    ResponseEntity<List<DriverDTO>> getDriversByVehicleId(@PathVariable Long vehicleId) {
        return new ResponseEntity<>(adminService.getDriversByVehicleId(vehicleId), HttpStatus.OK);
    }
}
