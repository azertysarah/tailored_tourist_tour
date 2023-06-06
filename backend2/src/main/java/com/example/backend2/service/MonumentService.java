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

    public List<Monument> findNearestMonuments(double inputLatitude, double inputLongitude, int limit) {
        List<Monument> monuments = monumentRepository.findAll();
        List<Monument> monumentsWithCoordinates = new ArrayList<>();

        // Calculate distances and sort the monuments based on their distance to the inputted monument
        monuments.forEach(monument -> {
            if(monument.getFields().getCoordinates() != null) {
                monument.setDistance(
                        calculateDistance(
                                inputLatitude,
                                inputLongitude,
                                monument.getFields().getCoordinates().get(0),
                                monument.getFields().getCoordinates().get(1)
                        )
                );
                monumentsWithCoordinates.add(monument);
            }
        });
        monumentsWithCoordinates.sort(Comparator.comparingDouble(Monument::getDistance));

        System.out.println(monumentsWithCoordinates.subList(0, Math.min(limit, monuments.size())));
        return monumentsWithCoordinates.subList(0, Math.min(limit, monuments.size()));
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
