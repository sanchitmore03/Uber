package com.san.Uber.Services.impl;

import com.san.Uber.Exceptions.ResourceNotFoundException;
import com.san.Uber.Repositories.RideRequestRepo;
import com.san.Uber.Services.RideRequestService;
import com.san.Uber.entities.RideRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {
    private final RideRequestRepo rideRequestRepo;
    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
            return rideRequestRepo.findById(rideRequestId).orElseThrow(() ->
                    new ResourceNotFoundException("Rider Request Not found wiht ride request id " + rideRequestId));

    }

    @Override
    public void upadate(RideRequest rideRequest) {
        RideRequest toSave = rideRequestRepo.findById(rideRequest.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Ride request Not found with id " +rideRequest.getId()));
                rideRequestRepo.save(rideRequest);
    }
}
