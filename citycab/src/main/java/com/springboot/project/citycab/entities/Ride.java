package com.springboot.project.citycab.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.project.citycab.entities.enums.PaymentMethod;
import com.springboot.project.citycab.entities.enums.RideStatus;
import com.springboot.project.citycab.entities.enums.Role;
import com.springboot.project.citycab.serializers.PointSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)  // Ignore null fields
@Table(indexes = {
        @Index(name = "idx_ride_rider", columnList = "rider_id"),
        @Index(name = "idx_ride_driver", columnList = "driver_id")
})
// What do u want to access from database on that we create indexes
// default index by rideId and we add more indexes for rider and driver so we can also search by using rider_id and driver_id
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideId;

    @JsonSerialize(using = PointSerializer.class)  // Use custom serializer
    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point pickupLocation;

    @JsonSerialize(using = PointSerializer.class)  // Use custom serializer
    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point dropOffLocation;

    @CreationTimestamp
    private LocalDateTime createdTime; // when a driver accepts the ride

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    private Double fare;

    private String otp;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    // Cancellation details
//    @JsonIgnore
//    @OneToOne(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
//    private CancelRide cancelRide;  // Reference to CancelRide entity

    // Rating details
//    @JsonIgnore
//    @OneToOne(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Rating rating;  // Reference to Rating entity

    @Override
    public String toString() {
        return "Ride{" +
                "rideId=" + rideId +
                ", pickupLocation=" + pickupLocation +
                ", dropOffLocation=" + dropOffLocation +
                ", createdTime=" + createdTime +
                ", paymentMethod=" + paymentMethod +
                ", rideStatus=" + rideStatus +
                ", fare=" + fare +
                ", otp='" + otp + '\'' +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                ", rider=" + rider + "\n" +
                ", driver=" + driver + "\n" +
//                ", cancelRide=" + cancelRide + "\n" +
//                ", rating=" + rating + "\n" +
                '}';
    }
}
