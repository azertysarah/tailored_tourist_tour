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


    public MonumentService(MonumentRepository monumentRepository){
        this.monumentRepository = monumentRepository;
    }

    public Monument getMonumentByName(String monumentName) {
        return this.monumentRepository.findByName(monumentName);
    }

    public List<Monument> getAllMonuments() {
        return monumentRepository.findAll();
    }

    public List<Monument> getMonumentByPeriod(String period) {
        return monumentRepository.findByPeriod(period);
    }


}
