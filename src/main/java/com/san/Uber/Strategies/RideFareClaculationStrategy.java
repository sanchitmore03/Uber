package com.san.Uber.Strategies;

import com.san.Uber.Dto.RideRequestDto;

public interface RideFareClaculationStrategy {
    double fareCalculation(RideRequestDto rideRequestDto);
}
