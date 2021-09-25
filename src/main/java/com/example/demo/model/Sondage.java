package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;

public class Sondage {
    private String nom, description;
    private Date dateLimite;
    private ArrayList<Date> dates;
    private ArrayList<String> commentaires;
    private ArrayList<Vote> votes;
}
