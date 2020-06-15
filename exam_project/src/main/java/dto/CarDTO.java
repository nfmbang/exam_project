/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import Entities.Car;

/**
 *
 * @author jonab
 */
public class CarDTO {
    private String id;
    private int year;
    private String make;
    private String Model;
    private double price;

    public CarDTO(Car car) {
        this.id = car.getId();
        this.year = car.getYear();
        this.make = car.getMake();
        this.Model = car.getModel();
        this.price = car.getPrice();
    }
    
    public String getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return Model;
    }

    public double getPrice() {
        return price;
    }
    
    
    
}
