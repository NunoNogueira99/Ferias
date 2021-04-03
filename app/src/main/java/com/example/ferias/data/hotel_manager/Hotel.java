package com.example.ferias.data.hotel_manager;

import com.example.ferias.data.common.Address;

import java.io.Serializable;

public class Hotel implements Serializable {

    ////////////////   DATA    ////////////////
    private String name;
    private String phone;
    private String description;
    private Address address;
    private String manager;
    private float price;
    private int rate;
    private float stars;
    private int total_rooms;
    private int rooms_occupied;
    private HotelMoods moods;
    private HotelFeature feature;


    public Hotel(){
    }

    public Hotel(String name, String phone, String description, Address address, String manager, float price, float stars, int total_rooms, HotelMoods moods, HotelFeature feature) {

        this.name = name;
        this.phone = phone;
        this.description = description;
        this.address = address;
        this.manager = manager;
        this.price = price;
        this.rate = 0;
        this.stars = stars;
        this.total_rooms = total_rooms;
        this.rooms_occupied = 0;
        this.moods = moods;
        this.feature = feature;
    }


    //////////////// GETS BEGIN ////////////////

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

    public String get_Manager() {
        return manager;
    }

    public float get_Price() {
        return price;
    }

    public int get_Rate() {
        return rate;
    }

    public float get_Stars() {
        return stars;
    }

    public int get_Total_Rooms() {
        return total_rooms;
    }

    public int get_Rooms_Occupied() {
        return rooms_occupied;
    }

    public HotelMoods getMoods() { return moods; }

    public HotelFeature getFeature() { return feature; }

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

    public void set_Manager(String manager) {
        this.manager = manager;
    }

    public void set_Price(float price) {
        this.price = price;
    }

    public void set_Rate(int rate) {
        this.rate = rate;
    }

    public void set_Stars(float stars) {
        this.stars = stars;
    }

    public void set_Total_Rooms(int total_rooms) {
        this.total_rooms = total_rooms;
    }

    public void set_Rooms_Occupied(int rooms_occupied) {
        this.rooms_occupied = rooms_occupied;
    }

    public void setMoods(HotelMoods moods) { this.moods = moods; }

    public void setHotelFeature(HotelFeature feature) { this.feature = feature; }

    //////////////// SETS END ////////////////



}
