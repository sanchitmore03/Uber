package com.san.Uber.Services;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.SignupDto;
import com.san.Uber.Dto.UserDto;
import org.springframework.stereotype.Service;


public interface AuthService {
    String login (String email , String passwrod);
    UserDto signup(SignupDto signupDto);
    DriverDto onboardNewDriver(Long userId);
}
