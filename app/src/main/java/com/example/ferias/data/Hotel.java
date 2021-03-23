package com.example.ferias.data;

import com.example.ferias.data.simple_user.Address;
import com.example.ferias.data.simple_user.User;

import java.io.Serializable;

public class Hotel implements Serializable {

    ////////////////   DATA    ////////////////
    private int hID;
    private String name;
    private String phone;
    private String description;
    private Address address;
    private User manager;
    private int price;
    private int rate;
    private int stars;
    private int rooms_available;
    private int rooms_occupied;

    // SEE HOW TO PUT THE AVAILABILITY PART


    public Hotel(){
    }

    public Hotel(int hID,String name, String phone, String description, Address address, User manager, int price, int rate, int stars, int rooms_available, int rooms_occupied) {

        this.hID = hID;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.address = address;
        this.manager = manager;
        this.price = price;
        this.rate = rate;
        this.stars = stars;
        this.rooms_available = rooms_available;
        this.rooms_occupied = rooms_occupied;
    }


    //////////////// GETS BEGIN ////////////////
    public int get_hID() {
        return hID;
    }

    public String get_Name() {
        return name;
    }

    public String get_Phone() {
        return phone;
    }

    public String get_Description() {
        return description;
    }

    public Address get_Address(){ return address;}

    public User get_Manager() {
        return manager;
    }

    public int get_Price() {
        return price;
    }

    public int get_Rate() {
        return rate;
    }

    public int get_Stars() {
        return stars;
    }

    public int get_Rooms_Available() {
        return rooms_available;
    }

    public int get_Rooms_Occupied() {
        return rooms_occupied;
    }


    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void set_Name(String name) {
        this.name = name;
    }

    public void set_Phone(String phone) {
        this.phone = phone;
    }

    public void set_Description(String description) {
        this.description = description;
    }

    public void set_Address(Address address){ this.address = address;}

    public void set_Manager(User manager) {
        this.manager = manager;
    }

    public void set_Price(int price) {
        this.price = price;
    }

    public void set_Rate(int rate) {
        this.rate = rate;
    }

    public void set_Stars(int stars) {
        this.stars = stars;
    }

    public void set_Rooms_Available(int rooms_available) {
        this.rooms_available = rooms_available;
    }

    public void set_Rooms_Occupied(int rooms_occupied) {
        this.rooms_occupied = rooms_occupied;
    }

    //////////////// SETS END ////////////////

    @Override
    public String toString() {
        return "\n" +"Hotel {"+ "\n" +
                "name='" + name + "\n" +
                ", hID='" + hID + "\n" +
                ", Address=" + address + "\n"+
                ", description='" + description + "\n" +
                ", phone='" + phone + "\n" +
                ", price='" + price + "\n" +
                ", rate='" + rate + "\n" +
                ", stars='" + stars + "\n" +
                ", rooms_available='" + rooms_available + "\n" +
                ", rooms_occupied='" + rooms_occupied + "\n" +
                '}';
    }




}

