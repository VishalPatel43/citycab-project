package com.springboot.project.citycab.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
//@ToString(exclude = "drivers")
//@ToString
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

    private Boolean available;

    //    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate registrationDate;
    private String registrationDate;

    @JsonIgnore
    @ManyToMany(mappedBy = "vehicles", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Driver> drivers;

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", numberPlate='" + numberPlate + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", model='" + model + '\'' +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", color='" + color + '\'' +
                ", drivers=" + drivers +
                '}';
    }
}
