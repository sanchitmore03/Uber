package com.san.Uber.Services.impl;

import com.san.Uber.Dto.DriverDto;
import com.san.Uber.Dto.SignupDto;
import com.san.Uber.Dto.UserDto;
import com.san.Uber.Repositories.UserRepo;
import com.san.Uber.Services.AuthService;
import com.san.Uber.Services.RiderService;
import com.san.Uber.entities.User;
import com.san.Uber.entities.enums.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final RiderService riderService;

    @Override
    public String login(String email, String passwrod) {

        return "";
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {

        User user = userRepo.findByEmail(signupDto.getEmail()).orElse(null);
        if(user != null){
            throw  new RuntimeException("Cannot signup , User already exists with email" + signupDto.getEmail());
        }
        User mappedUser = modelMapper.map(signupDto,User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        User savedUser = userRepo.save(mappedUser);
        // create user related entities
        riderService.createNewRider(savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId) {
        return null;
    }
}
