package com.springboot.project.citycab.entities;

import com.springboot.project.citycab.entities.enums.PaymentMethod;
import com.springboot.project.citycab.entities.enums.RideRequestStatus;
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
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideRequestId;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point pickupLocation;

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

//    @Override
//    public String toString() {
//        return "RideRequest{" +
//                "rideRequestId=" + rideRequestId +
//                ", pickupLocation=" + pickupLocation +
//                ", dropOffLocation=" + dropOffLocation +
//                ", requestedTime=" + requestedTime +
//                ", paymentMethod=" + paymentMethod +
//                ", rideRequestStatus=" + rideRequestStatus +
//                ", rider=" + rider +
//                '}';
//    }
}
