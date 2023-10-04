package com.sample.inventory.harness.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String guid;
    public String category;
    public String brand;
    public String model;
    public String description;
    public String releaseDate;

    public int availableStock;
    public double price;
    public int discount;

    public String company;
    public String address;
    public int zip;
    public String email;
};
