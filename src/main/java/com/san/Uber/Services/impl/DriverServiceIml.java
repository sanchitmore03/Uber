package com.san.Uber.Services.impl;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Dto.RiderDto;
import com.san.Uber.Exceptions.ResourceNotFoundException;
import com.san.Uber.Repositories.DriverRepo;
import com.san.Uber.Repositories.RideRequestRepo;
import com.san.Uber.Services.*;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.RideRequest;
import com.san.Uber.entities.User;
import com.san.Uber.entities.enums.RideRequestStatus;
import com.san.Uber.entities.enums.RideStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class DriverServiceIml implements DriverService {
    private final RideRequestService rideRequestService;
    private final DriverRepo driverRepo;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;
    private final RideRequestRepo rideRequestRepo;

    @Override
    public RideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new IllegalArgumentException("Driver cannot cancel ride they did not accept.");
        }

        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new IllegalStateException("Ride cannot be cancelled. Invalid status: " + ride.getRideStatus());
        }

        rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        updateDriverAvailability(driver,true);

        return modelMapper.map(ride, RideDto.class);
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


        Driver savedDriver = updateDriverAvailability(currentDriver,false);

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
        paymentService.createNewPayment(savedRide);
        ratingService.createNewRating(savedRide);
        return modelMapper.map(savedRide,RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("driver cannot start ride as he not accepted ride ");
        }

        if(!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Ride status is ONGOING as hence cannot ended:"+ride.getRideStatus());
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride,RideStatus.ENDED);
        updateDriverAvailability(driver,true);
        paymentService.processPayment(ride);

        return modelMapper.map(savedRide,RideDto.class);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("driver is not the oner of this ride ... ");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride status is not Ended as hence cannot start rating:"+ride.getRideStatus());
        }


        return ratingService.rateRider(ride,rating);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver driver = getCurrentDriver();
        return modelMapper.map(driver, DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(currentDriver,pageRequest).map(
                ride -> modelMapper.map(ride,RideDto.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("printing the current user of driver : "+user.getId());
        return driverRepo.findByUser(user).orElseThrow(() ->
         new ResourceNotFoundException("Driver not found with id "+ user.getId()));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepo.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepo.save(driver);
    }


}
