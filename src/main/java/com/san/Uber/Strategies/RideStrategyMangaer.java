package com.san.Uber.Strategies;

import com.san.Uber.Strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.san.Uber.Strategies.impl.DriverMatchingNearestDriverStrategy;
import com.san.Uber.Strategies.impl.RideFareSurgePricingFareCalculationStrategy;
import com.san.Uber.Strategies.impl.RiderFareDefaultFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyMangaer {
    private final DriverMatchingHighestRatedDriverStrategy driverMatchingHighestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy driverMatchingNearestDriverStrategy;
    private final RiderFareDefaultFareCalculationStrategy riderFareDefaultFareCalculationStrategy;
    private final RideFareSurgePricingFareCalculationStrategy rideFareSurgePricingFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double rating){
        if(rating >= 4.8){
            return driverMatchingHighestRatedDriverStrategy;
        }else return driverMatchingNearestDriverStrategy;
    }

    public RideFareClaculationStrategy rideFareClaculationStrategy(){
        LocalTime surgeStart = LocalTime.of(18,0);
        LocalTime surgEnd = LocalTime.of(21,0);
        LocalTime current = LocalTime.now();

        boolean isSerge = current.isAfter(surgeStart) && current.isBefore(surgEnd);
        if(isSerge){
            return  rideFareSurgePricingFareCalculationStrategy;
        }else{
            return riderFareDefaultFareCalculationStrategy;
        }

    }



}
