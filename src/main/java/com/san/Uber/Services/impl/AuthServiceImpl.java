package com.san.Uber.Services.impl;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.SignupDto;
import com.san.Uber.Dto.UserDto;
import com.san.Uber.Services.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String login(String email, String passwrod) {
        return "";
    }

    @Override
    public UserDto signup(SignupDto signupDto) {
        return null;
    }

    @Override
    public DriverDto onboardNewDriver(Long userId) {
        return null;
    }
}
