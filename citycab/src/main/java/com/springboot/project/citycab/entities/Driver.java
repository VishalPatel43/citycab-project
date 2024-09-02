package com.springboot.project.citycab.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    private Double rating;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point currentLocation;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Vehicle information
    // One to One mapping with Vehicle Entity
    // Vehicle Type, Vehicle Number, Vehicle Model, Vehicle Color, etc.

}
