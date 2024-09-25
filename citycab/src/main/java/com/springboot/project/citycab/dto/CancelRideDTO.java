package com.springboot.project.citycab.dto;

import com.springboot.project.citycab.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelRideDTO {

    private Long cancelRideId;

    private String reason;

    private Role cancelledBy;

    private LocalDateTime cancelledAt;

    private RideDTO ride;
}
