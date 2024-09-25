package com.springboot.project.citycab.entities;

import com.springboot.project.citycab.entities.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CancelRide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cancelRideId;

    @NotNull(message = "Must provide a reason for cancelling the ride")
    private String reason;

    @Enumerated(EnumType.STRING)
    private Role cancelledBy;

    private LocalDateTime cancelledAt;

    // Relationship
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id", referencedColumnName = "rideId")
    private Ride ride;  // One-to-one relation with Ride entity, since a ride can only be cancelled once
}
