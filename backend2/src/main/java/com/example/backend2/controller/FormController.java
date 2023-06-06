package com.example.backend2.controller;

import com.example.backend2.dto.Monument;
import com.example.backend2.model.Data;
import com.example.backend2.repository.MonumentRepository;
import com.example.backend2.service.MonumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.round;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
public class FormController {
    private final MonumentService monumentService;
    private static final double EARTH_RADIUS = 6371; // Radius of the Earth in kilometers

    @Autowired
    public FormController(MonumentService monumentService) {
        this.monumentService = monumentService;
    }
    @PostMapping("/tours")
    public ResponseEntity<List<Monument>> handleFormSubmission(@RequestBody Data data) {
        // Data form values
        String monumentName = data.getMonumentName();
        String region = data.getRegion();
        String period =  data.getPeriod();
        int time = data.getTime();
        boolean needRealTime = data.isNeedRealTime();

        // Get the input monument from the database
        Monument monument = monumentService.getMonumentByName(monumentName);

        // Get all the monuments from the database
        List<Monument> monuments = monumentService.getAllMonuments();

        // Filter the results if there is a requested period
        if(period != "") monuments = filterByPeriod(period, monuments);

        // Get the nearest monuments from the requested monument
        List<Monument> nearestMonuments = findNearestMonuments(monument, monuments);

        // Delete the ones that are not in the selected region
        //if(region != "") nearestMonuments = filterByRegion(region, nearestMonuments);

        // Take the travel time into account
        if(needRealTime) nearestMonuments = getRealTour(nearestMonuments, time);

        // Send response
        return new ResponseEntity<>(nearestMonuments, HttpStatus.OK);
    }

    public List<Monument> filterByPeriod(String period, List<Monument> monuments){
        List<Monument> filteredMonuments = monumentService.getMonumentByPeriod(period);
        for (Monument monument : monuments){
            if(monument.getFields().getSiecle() != null && monument.getFields().getSiecle().contains(period)) {
                filteredMonuments.add(monument);
            }
        }
        return filteredMonuments;
    }

    public List<Monument> getRealTour(List<Monument> monuments, int time){
        double timeLimit = time*16; // time*24 for days-hours conversion and time*8 for sleeping time per day
        List<Monument> realTour = new ArrayList<>();
        Monument initialMonument = monuments.remove(0);
        realTour.add(initialMonument);
        while(!monuments.isEmpty() || timeLimit>0) {
            Monument currentMonument = monuments.remove(0);
            double pathTime = 5/currentMonument.getDistance(); // We suppose that the average speed of a walking person is equal to 5km/h
            timeLimit = timeLimit - round(pathTime)- 3; // We suppose that it takes 3 hours to visit a monument
            realTour.add(currentMonument);
            monuments = findNearestMonuments(currentMonument, monuments);
        }
        return realTour;
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