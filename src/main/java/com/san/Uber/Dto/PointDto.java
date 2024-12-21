package com.san.Uber.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class PointDto {
    private double[] coordinates;
    private String type = "Point";


    public PointDto(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
