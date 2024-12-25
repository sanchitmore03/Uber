package com.san.Uber.Services;

import com.san.Uber.Exceptions.ResourceNotFoundException;
import com.san.Uber.Repositories.UserRepo;
import com.san.Uber.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(null
        );
    }

    public User getUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("user id not found with id "+userId));
    }
}
