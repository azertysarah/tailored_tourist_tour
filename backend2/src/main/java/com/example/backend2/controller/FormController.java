package com.example.backend2.controller;

import com.example.backend2.db.Monument;
import com.example.backend2.model.FormData;
import com.example.backend2.repository.MonumentRepository;
import jakarta.xml.bind.SchemaOutputResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
public class FormController {

    private final MonumentRepository monumentRepository;

    @Autowired
    public FormController(MonumentRepository monumentRepository) {
        this.monumentRepository = monumentRepository;
    }
    @PostMapping("/tours")
    public ResponseEntity<List<String>> handleFormSubmission(@RequestBody FormData formData) {
        String location = formData.getLocation();
        String period =  formData.getPeriod();
        int time = formData.getTime();
        int budget = formData.getBudget();


        List<Monument> monumentList = monumentRepository
                .findByCommuneAndBySiecle(location, period);
        //Search Abbeville, 2e moitié 18e siècle
        List<String> centuriesList = monumentList.stream()
                .map(Monument::getNom)
                .limit(time * 2L)
                .toList();
        System.out.println(centuriesList);
        System.out.println(location + period + time + budget);
        System.out.println(monumentList);


        return new ResponseEntity<>(centuriesList, HttpStatus.OK);
    }
}