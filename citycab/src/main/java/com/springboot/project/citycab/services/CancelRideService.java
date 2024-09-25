package com.springboot.project.citycab.services;


import com.springboot.project.citycab.entities.CancelRide;
import com.springboot.project.citycab.entities.Driver;
import com.springboot.project.citycab.entities.Ride;
import com.springboot.project.citycab.entities.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CancelRideService {
    Ride cancelRide(Ride ride, String reason, Role cancelledBy);

    Page<CancelRide> getCancelRideByRole(Role cancelledBy, PageRequest pageRequest);
}
