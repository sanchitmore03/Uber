package com.san.Uber.Controller;

import com.san.Uber.Dto.RideDto;
import com.san.Uber.Dto.RideStartDto;
import com.san.Uber.Services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
public class DriverController {
    private final DriverService driverService;

    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long rideRequestId) {

        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }
    @PostMapping("/startRide/{rideRequestId}")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideRequestId,
                                             @RequestBody RideStartDto rideStartDto) {

        return ResponseEntity.ok(driverService.startRide(rideRequestId,rideStartDto.getOtp()));
    }


}
