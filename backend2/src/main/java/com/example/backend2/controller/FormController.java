package com.example.backend2.controller;

import com.example.backend2.dto.Monument;
import com.example.backend2.dto.TourMonument;
import com.example.backend2.model.FormData;
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
    public ResponseEntity<List<Monument>> handleFormSubmission(@RequestBody FormData formData) {
        // Data form values
        String monumentName = formData.getMonumentName();
        String commune = formData.getCommune();
        String period =  formData.getPeriod();
        int time = formData.getTime();

        // Get the input monument from the database
        Monument monument = this.monumentRepository.findByName(monumentName);

        // Get the coordinates of the monument
        List<Double> coordinates = monument.getFields().getCoordinates();
        Double latitude = coordinates.get(0);
        Double longitude = coordinates.get(1);

        // Get the nearest monuments
        List<Monument> nearestMonuments = this.monumentService.findNearestMonuments(latitude, longitude, time*2);
        //System.out.println(nearestMonuments.toString());
        //List<TourMonument> tourMonuments = getInShape(nearestMonuments);
        //System.out.println(tourMonuments);

        // Delete the ones that are not in the selected commune
        // Delete the ones that are not from the selected period
        // Do something about


        /*List<Monument> monumentList = monumentRepository
                .findByCommuneAndBySiecle(commune, period);
        //Search Abbeville, 2e moitié 18e siècle
        List<String> centuriesList = monumentList.stream()
                .map(Monument::getNom)
                .limit(time * 2L)
                .toList();
        System.out.println(centuriesList);
        System.out.println(commune + period + time);
        System.out.println(monumentList);*/

        return new ResponseEntity<>(nearestMonuments, HttpStatus.OK);
    }

    /*private static List<TourMonument> getInShape(List<Monument> monuments) {
        List<TourMonument> tourMonumentsList = new ArrayList<>();
        for(Monument monument : monuments) {
            double latitude = monument.getFields().getCoordinates().get(0);
            double longitude = monument.getFields().getCoordinates().get(1);
            String name = monument.getFields().getName();

            TourMonument tourMonument = new TourMonument(latitude, longitude, name);
            tourMonumentsList.add(tourMonument);
        }
        return tourMonumentsList;
    }*/
}