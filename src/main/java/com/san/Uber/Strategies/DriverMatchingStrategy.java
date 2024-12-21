package com.san.Uber.Strategies;

import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.entities.Driver;

import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findMatchingDriver(RideRequestDto rideRequestDto);
}