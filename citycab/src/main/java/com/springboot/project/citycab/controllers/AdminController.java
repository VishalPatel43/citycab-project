package com.springboot.project.citycab.controllers;

import com.springboot.project.citycab.dto.DriverDTO;
import com.springboot.project.citycab.dto.RiderDTO;
import com.springboot.project.citycab.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
