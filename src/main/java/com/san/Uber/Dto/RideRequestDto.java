package com.san.Uber.Dto;

import com.san.Uber.entities.enums.PayementMethod;
import com.san.Uber.entities.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDto {
    private Long id;


    private PointDto pickupLocation;

    private PointDto dropOffLocation;

    private LocalDateTime requestedTime;


    private RiderDto rider;

    private Double fare;

    private PayementMethod payementMethod;


    private RideRequestStatus rideRequestStatus;
}
