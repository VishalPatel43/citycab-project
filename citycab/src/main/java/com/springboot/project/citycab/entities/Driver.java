package com.springboot.project.citycab.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.project.citycab.serializers.PointSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(indexes = {
        @Index(name = "idx_driver_vehicle_id", columnList = "vehicleId")
})
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    private Double avgRating;

    private Boolean available;

    private String vehicleId;


    @JsonSerialize(using = PointSerializer.class)  // Use custom serializer
    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point currentLocation;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "driver_vehicle",
            joinColumns = @JoinColumn(name = "driver_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    private Set<Vehicle> vehicles;

    @ManyToMany(mappedBy = "drivers")
    @JsonIgnore  // To prevent infinite recursion during JSON serialization
    private List<RideRequest> rideRequests;

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId + "\n" +
                ", avgRating=" + avgRating + "\n" +
                ", available=" + available + "\n" +
                ", vehicleId='" + vehicleId + '\'' + "\n" +
                ", currentLocation=" + currentLocation + "\n" +
                ", user=" + user + "\n" +
                ", address=" + address + "\n" +
                ", vehicles=" + vehicles + "\n" +
                '}';
    }
}
