package com.example.backend2.service;

import com.example.backend2.dto.Monument;
import com.example.backend2.repository.MonumentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MonumentService {
    private final MonumentRepository monumentRepository;
    private static final double EARTH_RADIUS = 6371; // Radius of the Earth in kilometers

    public MonumentService(MonumentRepository monumentRepository){
        this.monumentRepository = monumentRepository;
    }

    public List<Monument> findNearestMonuments(Monument monument, List<Monument> monuments) {
        // Maximum distance is set to for 1 hour walking at the speed of 5km/h
        double maximumDistance = 10;

        // Get the coordinates of the monument
        List<Double> coordinates = monument.getFields().getCoordinates();
        Double monumentLatitude = coordinates.get(0);
        Double monumentLongitude = coordinates.get(1);

        // To prevent having monuments that has no coordinates
        List<Monument> nearestMonuments = new ArrayList<>();

        // Calculate distances and sort the monuments based on their distance to the inputted monument
        monuments.forEach(item -> {
            if(item.getFields().getCoordinates() != null) {
                item.setDistance(
                        calculateDistance(
                                monumentLatitude,
                                monumentLongitude,
                                item.getFields().getCoordinates().get(0),
                                item.getFields().getCoordinates().get(1)
                        )
                );
                if(item.getDistance() <= maximumDistance) nearestMonuments.add(item);
            }
        });
        // Sort list from most near monuments to most far
        nearestMonuments.sort(Comparator.comparingDouble(Monument::getDistance));
        return nearestMonuments;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert the latitude and longitude from degrees to radians
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // Calculate the differences in latitude and longitude in radians
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        // Calculate the central angle (c)
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance using the Earth's radius (assuming a spherical Earth)
        double distance = EARTH_RADIUS * c;

        return distance;
    }

}
