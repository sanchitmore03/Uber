package com.san.Uber.Services.impl;

import com.san.Uber.Services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {
    private static final String OSRM_API_BASE_URL = "http://router.project-osrm.org/route/v1/driving/";
    @Override
    public double calculateDistance(org.locationtech.jts.geom.Point src, Point dest) {
        String uri = src.getX()+","+src.getY()+";"+dest.getX()+","+dest.getY();
        try{
            OSRMResponseDto osrmResponseDto =RestClient.builder()
                    .baseUrl(OSRM_API_BASE_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDto.class);
            return osrmResponseDto.getRoutes().get(0).getDistance() / 1000.0;
        }catch(Exception e){
            throw new RuntimeException("Error getting message from osrm"+e.getMessage());
        }

    }
}

@Data
class OSRMResponseDto{
    private List<OSRMRoutes> routes;
}
@Data
class OSRMRoutes{
    private Double distance;
}

