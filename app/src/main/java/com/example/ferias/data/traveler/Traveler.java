package com.example.ferias.data.traveler;

import com.example.ferias.data.common.User;
import com.example.ferias.data.hotel_manager.Hotel;

import java.io.Serializable;
import java.util.List;

public class Traveler extends User implements Serializable {

    ////////////////     BOOKINGS     ////////////////
    private List<Booking> bookings;

    public Traveler(){
        super();
    }

    public Traveler(String name, String surname, String phone, String email, boolean isGoogle) {
        super(name, surname, phone, email, isGoogle);
    }

    public Traveler(String name, String surname, String email, String phone, String password) {
        super(name, surname, email, phone, password);
    }

    //////////////// GETS BEGIN ////////////////
    public List<Booking> getHotels() {
        return bookings;
    }
    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void setHotels(List<Booking> hotels) {
        this.bookings = hotels;
    }

    public void addHotel(Booking hotel) {
        this.bookings.add(hotel);
    }

    public void removeHotelbyIndex(int index) {
        this.bookings.remove(index);
    }

    public void removeHotelbyObject(Hotel hotel) {
        this.bookings.remove(hotel);
    }
    //////////////// SETS END ////////////////


}
