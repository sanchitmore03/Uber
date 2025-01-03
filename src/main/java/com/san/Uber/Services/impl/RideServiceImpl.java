package com.san.Uber.Services.impl;

import com.san.Uber.Repositories.RideRepo;
import com.san.Uber.Services.RideRequestService;
import com.san.Uber.Services.RideService;
import com.san.Uber.entities.Driver;
import com.san.Uber.entities.Ride;
import com.san.Uber.entities.RideRequest;
import com.san.Uber.entities.Rider;
import com.san.Uber.entities.enums.RideRequestStatus;
import com.san.Uber.entities.enums.RideStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService
{

    private final RideRepo rideRepo;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepo.findById(rideId).orElseThrow(() ->
                new RuntimeException("ride id not found.."));
    }



    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);
        Ride ride  = modelMapper.map(rideRequest,Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(GenerateRandomOtp());
        ride.setId(null);
        rideRequestService.upadate(rideRequest);
        return rideRepo.save(ride);

    }



    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
       return rideRepo.save(ride);

    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepo.findByRider(rider,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepo.findByDriver(driver,pageRequest);
    }

    @Override
    public Ride createNewRider(RideRequest rideRequest, Driver currentDriver) {
        return null;
    }

    //    @Override
//    public Ride createNewRide(RideRequest rideRequest, Driver currentDriver) {
//        Rider rider = Rider
//                .builder()
//                .user(user)
//                .rating(0.0)
//                .build();
//        return riderRepository.save(rider);
//    }
    private String GenerateRandomOtp(){
        Random random = new Random();
        int OtpInt = random.nextInt(1000);
        return String.format("%04d",OtpInt);
    }
}
