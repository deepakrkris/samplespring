package com.sample.inventory.harness.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String guid;
    public String customerName;
    public String category;
    public String brand;
    public String model;

    public double rating;
    public String comment;
};
