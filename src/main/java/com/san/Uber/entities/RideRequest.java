package com.san.Uber.entities;

import com.san.Uber.entities.enums.PayementMethod;
import com.san.Uber.entities.enums.RideRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "Geometry(Point,4326)")
    private org.locationtech.jts.geom.Point pickupLocation;
    @Column(columnDefinition = "Geometry(Point,4326)")
    private Point dropOffLocation;

    @CreationTimestamp
    private LocalDateTime requestedTime;

    @ManyToOne(fetch  = FetchType.LAZY)
    private Rider rider;

    @Enumerated(EnumType.STRING)
    private PayementMethod payementMethod;

    @Enumerated(EnumType.STRING)
    private RideRequestStatus rideRequestStatus;



}