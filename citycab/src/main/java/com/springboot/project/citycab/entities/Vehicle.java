package com.springboot.project.citycab.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(indexes = {
        @Index(name = "idx_vehicle_number_plate", columnList = "numberPlate"),
        @Index(name = "idx_vehicle_registration_number", columnList = "registrationNumber")
})
@ToString(exclude = "drivers")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    // name
    private String numberPlate;

    private String registrationNumber;

    private String ownerName;

    private String model;

    private String type;

    private int capacity;

    private String color;

    @JsonIgnore
    @ManyToMany(mappedBy = "vehicles")
    private Set<Driver> drivers;

}
