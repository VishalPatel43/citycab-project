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
@Table (indexes = {
        @Index(name = "driver_aadhaar_card_number_index", columnList = "aadhaarCardNumber", unique = true),
        @Index(name = "driver_license_number_index", columnList = "drivingLicenseNumber", unique = true)
})
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    private Double avgRating;

    private Boolean available;

    @JsonSerialize(using = PointSerializer.class)  // Use custom serializer
    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point currentLocation;

    private Long aadhaarCardNumber;

    private String drivingLicenseNumber;

//    private String licenseExpiryDate;

    // private Boolean status;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "driver_vehicle",
            joinColumns = @JoinColumn(name = "driver_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    private Set<Vehicle> vehicles;

    @OneToOne
    @JoinColumn(name = "current_vehicle_id")
    private Vehicle currentVehicle;

    @ManyToMany(mappedBy = "drivers")
    @JsonIgnore  // To prevent infinite recursion during JSON serialization
    private List<RideRequest> rideRequests;

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", avgRating=" + avgRating +
                ", available=" + available +
                ", currentLocation=" + currentLocation +
                ", user=" + user +
                ", address=" + address +
//                ", vehicles=" + vehicles +
                '}';
    }
}
