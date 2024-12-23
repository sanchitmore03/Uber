package com.san.Uber.Strategies;

import com.san.Uber.entities.RideRequest;

public interface RideFareClaculationStrategy {
    double RIDE_FARE_MULTIPLIER = 10;
    double fareCalculation(RideRequest rideRequest);
}
