package com.springboot.project.citycab.services;


import com.springboot.project.citycab.constants.enums.Role;
import com.springboot.project.citycab.entities.CancelRide;
import com.springboot.project.citycab.entities.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CancelRideService {
    CancelRide cancelRide(Ride ride, String reason, Role cancelledBy);

    Page<CancelRide> getCancelRideByRole(Role cancelledBy, PageRequest pageRequest);
}
