package com.san.Uber.Dto;

import com.san.Uber.entities.enums.PaymentMethod;
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

    private Location pickupLocation;

    private Location dropOffLocation;

    private LocalDateTime requestedTime;

    private RiderDto rider;

    private Double fare;

    private PaymentMethod paymentMethod;

    private RideRequestStatus rideRequestStatus;



    public static class Location {
        private String type;
        private double[] coordinates;
        private String address;
    }
}
