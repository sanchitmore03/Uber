package com.san.Uber.Services.impl;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Services.RideService;
import com.san.Uber.Services.RiderService;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideServiceImpl implements RideService
{

    @Override
    public Ride getRideById(Long rideId) {
        return null;
    }

    @Override
    public void matchWithDrivers(RideRequestDto rideRequestDto) {

    }

    @Override
    public Ride createNewRide(RideRequestDto rideRequestDto, Driver driver) {
        return null;
    }

    @Override
    public Ride updateRideStatus(Long rideId, RideStatus rideStatus) {
        return null;
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Long riderId, PageRequest pageRequest) {
        return null;
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Long driverId, PageRequest pageRequest) {
        return null;
    }
}
