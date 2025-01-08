package com.san.Uber.Services;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DriverService {
    RideDto cancelRide(Long rideRequestId);
    RideDto acceptRide(Long rideId);
    RideDto startRide(Long rideId,String otp);
    RideDto endRide(Long rideId);
    RiderDto rateRider(Long rideId,Integer rating);
    DriverDto getMyProfile();
    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    Driver updateDriverAvailability(Driver driver,boolean available);

    Driver createNewDriver(Driver driver);



}
