package com.san.Uber.Services;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.entities.Driver;

import java.util.List;

public interface DriverService {
    RideDto cancelRide(Long rideRequestId);
    RideDto acceptRide(Long rideId);
    RideDto startRide(Long rideId,String otp);
    RideDto endRide(Long rideId);
    RiderDto rateRider(Long rideId,Integer rating);
    DriverDto getMyProfile();
    List<RideDto> getAllMyrides();

    Driver getCurrentDriver();


}
