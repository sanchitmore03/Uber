package com.san.Uber.Services.impl;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.Exceptions.ResourceNotFoundException;
import com.san.Uber.Repositories.DriverRepo;
import com.san.Uber.Services.DriverService;
import com.san.Uber.Services.RideRequestService;
import com.san.Uber.Services.RideService;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.RideRequest;
import com.san.Uber.entities.enums.RideRequestStatus;
import com.san.Uber.entities.enums.RideStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DriverServiceIml implements DriverService {
    private final RideRequestService rideRequestService;
    private final DriverRepo driverRepo;
    private final RideService rideService;
    private final ModelMapper modelMapper;

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);


        // Ensure the ride request status is valid
        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            throw new IllegalStateException("Ride Request cannot be accepted. Current status: " + rideRequest.getRideRequestStatus());
        }

        // Fetch the current driver dynamically
        Driver currentDriver = getCurrentDriver();

        // Check if the driver is available
        if (!currentDriver.getAvailable()) {
            throw new IllegalStateException("Driver cannot accept the ride due to unavailability.");
        }


        currentDriver.setAvailable(false);
       Driver savedDriver = driverRepo.save(currentDriver);

        // Create the new ride
        Ride ride = rideService.createNewRide(rideRequest, savedDriver);


        // Return the mapped DTO
        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    public RideDto startRide(Long rideId , String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("driver cannot start ride as he not accepted ride ");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride status is not confirmed as hence cannot started:"+ride.getRideStatus());
        }

        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("otp is not valid "+otp);
        }
        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide  =rideService.updateRideStatus(ride,RideStatus.ONGOING);

        return modelMapper.map(savedRide,RideDto.class);
    }

    @Override
    public RideDto endRide(Long rideId) {
        return null;
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        return null;
    }

    @Override
    public DriverDto getMyProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllMyrides() {
        return List.of();
    }

    @Override
    public Driver getCurrentDriver() {
        return driverRepo.findById(2L).orElseThrow(() ->
         new ResourceNotFoundException("Driver not found with id "+ 2));
    }
}
