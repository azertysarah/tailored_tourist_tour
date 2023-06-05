package com.example.backend2.db;

import java.util.List;

public class Fields {

    private List<Double> p_coordonnees;
    private String siecle;

    private String commune;

    public String getAppellation_courante() {
        return appellation_courante;
    }

    private String appellation_courante;

    @Override
    public String toString() {
        return "Fields{" +
                "p_coordonnees=" + p_coordonnees +
                ", siecle='" + siecle + '\'' +
                ", commune='" + commune + '\'' +
                ", appellation_courante:'" + appellation_courante + '\'' +
                '}';
    }
}
