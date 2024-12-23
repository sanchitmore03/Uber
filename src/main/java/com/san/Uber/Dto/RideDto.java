package com.san.Uber.Dto;


import com.san.Uber.entities.enums.PayementMethod;
import com.san.Uber.entities.enums.RideStatus;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
public class RideDto {
    private Long id;

    private PointDto pickupLocation;
    private PointDto dropOffLocation;

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
