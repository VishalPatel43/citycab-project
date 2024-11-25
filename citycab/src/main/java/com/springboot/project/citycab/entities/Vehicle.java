package com.springboot.project.citycab.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vehicle vehicle)) return false;
        return capacity == vehicle.capacity && Objects.equals(vehicleId, vehicle.vehicleId) && Objects.equals(numberPlate, vehicle.numberPlate) && Objects.equals(registrationNumber, vehicle.registrationNumber) && Objects.equals(ownerName, vehicle.ownerName) && Objects.equals(model, vehicle.model) && Objects.equals(type, vehicle.type) && Objects.equals(color, vehicle.color) && Objects.equals(available, vehicle.available) && Objects.equals(registrationDate, vehicle.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, numberPlate, registrationNumber, ownerName, model, type, capacity, color, available, registrationDate);
    }
}
