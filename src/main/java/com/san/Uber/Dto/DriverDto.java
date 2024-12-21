package com.san.Uber.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.UserDatabase;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {
    private UserDto user;
    private Double rating;
}
