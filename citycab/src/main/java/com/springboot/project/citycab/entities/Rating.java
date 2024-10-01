package com.springboot.project.citycab.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ride_id"})
        },
        indexes = {
                @Index(name = "idx_rating_rider", columnList = "rider_id"),
                @Index(name = "idx_rating_driver", columnList = "driver_id")
//        @Index(name = "idx_rating_ride", columnList = "ride_id")
        })
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(length = 500)
    private String comment;

    private LocalDateTime ratingDate;

    //    @Column(nullable = false)
    private Integer driverRating; // Assuming rating is between 1-5

    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", comment='" + comment + '\'' +
                ", ratingDate=" + ratingDate +
                ", driverRating=" + driverRating +
                ", rider=" + rider + "\n" +
                ", driver=" + driver + "\n" +
                '}';
    }
}

//10 ratingsCount -> 4.0
//new rating 4.6
//updated rating
//new rating 44.6/11 -> 4.05