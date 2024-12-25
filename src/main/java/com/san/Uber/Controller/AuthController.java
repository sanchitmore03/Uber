package com.san.Uber.Controller;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.SignupDto;
import com.san.Uber.Dto.UserDto;
import com.san.Uber.Dto.onBoardDriverDto;
import com.san.Uber.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    ResponseEntity<UserDto> signUp(@RequestBody SignupDto signupDto) {
        return new ResponseEntity<>(authService.signup(signupDto), HttpStatus.CREATED);
    }

    @PostMapping("/onBoardNewDriver/{userId}")
    ResponseEntity<DriverDto> onBoardNewDriver(@PathVariable Long userId,@RequestBody onBoardDriverDto onBoardDriverDto){
        return new ResponseEntity<>(authService.onboardNewDriver(userId,onBoardDriverDto.getVehicleId()),HttpStatus.CREATED);

    }


}