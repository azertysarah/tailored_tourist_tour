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
import java.util.List;

import static java.lang.Math.round;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
public class FormController {
    private final MonumentRepository monumentRepository;
    private final MonumentService monumentService;

    @Autowired
    public FormController(MonumentRepository monumentRepository, MonumentService monumentService) {
        this.monumentRepository = monumentRepository;
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
        Monument monument = this.monumentRepository.findByName(monumentName);

        // Get all the monuments from the database
        List<Monument> monuments = monumentRepository.findAll();

        // Filter the results if there is a requested period
        if(period != "") monuments = filterByPeriod(period, monuments);

        /*List<Monument> monuments;
        if(period != "") {
            List<Monument> filteredMonuments = monumentRepository.findByPeriod(period);
            monuments = filterByPeriod(period, filteredMonuments);
        } else {
            monuments = monumentRepository.findAll();
        }*/

        // Get the nearest monuments from the requested monument
        List<Monument> nearestMonuments = this.monumentService.findNearestMonuments(monument, monuments);
        // System.out.println(nearestMonuments.toString());

        // Delete the ones that are not in the selected region
        //if(region != "") nearestMonuments = filterByRegion(region, nearestMonuments);

        // Take the travel time into account
        if(needRealTime) nearestMonuments = getRealTour(nearestMonuments, time);

        // Send response
        return new ResponseEntity<>(nearestMonuments, HttpStatus.OK);
    }

    public List<Monument> filterByPeriod(String period, List<Monument> monuments){
        List<Monument> filteredMonuments = monumentRepository.findByPeriod(period);
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
            monuments = monumentService.findNearestMonuments(currentMonument, monuments);
        }
        return realTour;
    }
}