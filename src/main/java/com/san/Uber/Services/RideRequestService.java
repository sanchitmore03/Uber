package com.san.Uber.Services;


import com.san.Uber.entities.RideRequest;

public interface RideRequestService {

    RideRequest findRideRequestById(Long rideRequestId);

    void upadate(RideRequest rideRequest);
}
