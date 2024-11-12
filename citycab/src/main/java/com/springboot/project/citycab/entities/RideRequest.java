package com.springboot.project.citycab.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.project.citycab.constants.enums.PaymentMethod;
import com.springboot.project.citycab.constants.enums.RideRequestStatus;
import com.springboot.project.citycab.serializers.PointSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(
        indexes = {
                @Index(name = "idx_ride_request_rider", columnList = "rider_id")
        }
)
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideRequestId;

    @JsonSerialize(using = PointSerializer.class)  // Use custom serializer
    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point pickupLocation;

    @JsonSerialize(using = PointSerializer.class)  // Use custom serializer
    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point dropOffLocation;

    @CreationTimestamp
    private LocalDateTime requestedTime;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideRequestStatus rideRequestStatus;

    private Double fare;

    @ManyToOne(fetch = FetchType.LAZY) // Many RideRequests can be associated with one Rider
    @JoinColumn(name = "rider_id")
    private Rider rider; // One Rider can have many RideRequests

    @ManyToMany
    @JoinTable(
            name = "ride_request_driver",
            joinColumns = @JoinColumn(name = "ride_request_id"),
            inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    private List<Driver> drivers;

    @Override
    public String toString() {
        return "RideRequest{" +
                "rideRequestId=" + rideRequestId +
                ", pickupLocation=" + pickupLocation +
                ", dropOffLocation=" + dropOffLocation +
                ", requestedTime=" + requestedTime +
                ", paymentMethod=" + paymentMethod +
                ", rideRequestStatus=" + rideRequestStatus + "\n" +
                ", rider=" + rider +
                '}';
    }
}
