package com.san.Uber.Strategies.impl;

import com.san.Uber.Services.DistanceService;
import com.san.Uber.Strategies.RideFareClaculationStrategy;
import com.san.Uber.entities.RideRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderFareDefaultFareCalculationStrategy implements RideFareClaculationStrategy {

    private final DistanceService distanceService;
    @Override
    public double fareCalculation(RideRequest rideRequest) {
        double distance = distanceService.calculateDistance(rideRequest.getPickupLocation(),rideRequest.getDropOffLocation());
        return distance*RIDE_FARE_MULTIPLIER;
    }
}
