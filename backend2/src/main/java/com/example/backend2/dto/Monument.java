package com.example.backend2.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "monuments")
public class Monument {
    @Id
    private ObjectId dataset_id;
    private Fields fields;
    private double distance; // We add this property to then calculate the distance of a monument according to the input monument

    // Getter and setters
    public Fields getFields() {
        return fields;
    }
    public void setFields(Fields fields) {
        this.fields = fields;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public static class Fields {
        private List<Double> p_coordonnees;
        private String region;
        private String appellation_courante;
        private String siecle;
        private String code_departement;
        private String departement;


        private String commune;

        // Getters and setters

        public List<Double> getCoordinates() {
            return p_coordonnees;
        }
        public String getName() {
            return appellation_courante;
        }
        public String getRegion() {
            return region;
        }
        public String getSiecle() {
            return siecle;
        }
    }

    @Override
    public String toString() {
        return "{" +
                "name: '" + fields.appellation_courante +
                "', coordinates: " + fields.p_coordonnees +
                "', region: " + fields.region +
                "', period: " + fields.siecle +
                '}';
    }
}
