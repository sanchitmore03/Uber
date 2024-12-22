package com.san.Uber.Strategies.impl;

import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Strategies.DriverMatchingStrategy;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.RideRequest;

import java.util.List;

public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return List.of();
    }
}
