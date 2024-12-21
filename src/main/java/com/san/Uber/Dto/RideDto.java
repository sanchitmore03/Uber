package com.san.Uber.Dto;


import com.san.Uber.entities.enums.PayementMethod;
import com.san.Uber.entities.enums.RideStatus;

import org.locationtech.jts.geom.Point;

import java.awt.*;
import java.time.LocalDateTime;

public class RideDto {
    private Long id;

    private org.geolatte.geom.Point pickupLocation;
    private Point dropOffLocation;

    private LocalDateTime createdTime;

    private RiderDto rider;
    private DriverDto driver;

    private String otp;

    private PayementMethod payementMethod;

    private RideStatus rideStatus;

    private Double fare;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
