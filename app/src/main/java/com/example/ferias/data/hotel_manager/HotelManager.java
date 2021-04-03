package com.example.ferias.data.hotel_manager;

import com.example.ferias.data.common.Address;
import com.example.ferias.data.common.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HotelManager extends User implements Serializable {

    ////////////////     HOTELS     ////////////////
    private List<Hotel> hotels;

    public HotelManager(){
        this.hotels = new ArrayList<>();
    }

    public HotelManager(String name, String surname, String phone, String email, boolean isGoogle) {
        super(name, surname, phone, email, isGoogle);
        this.hotels = new ArrayList<>();
    }

    public HotelManager(String name, String surname, String email, String phone, String password) {
        super(name, surname, email, phone, password);
        this.hotels = new ArrayList<>();
    }

    //////////////// GETS BEGIN ////////////////
    public List<Hotel> getHotels() {
        return hotels;
    }
    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    public void addHotel(Hotel hotel) {
        this.hotels.add(hotel);
    }

    public void removeHotelbyIndex(int index) {
        this.hotels.remove(index);
    }

    public void removeHotelbyObject(Hotel hotel) {
        this.hotels.remove(hotel);
    }
    //////////////// SETS END ////////////////

}
