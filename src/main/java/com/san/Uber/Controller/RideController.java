package com.san.Uber.Controller;

import com.san.Uber.Dto.RideRequestDto;
import com.san.Uber.Services.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/request")
    public ResponseEntity<?> requestRide(@RequestBody RideRequestDto rideRequestDto) {
        return ResponseEntity.ok(rideService.createRideRequest(rideRequestDto));
    }
} 