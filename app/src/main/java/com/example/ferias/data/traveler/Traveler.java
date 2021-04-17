package com.example.ferias.data.traveler;

import com.example.ferias.data.common.User;
import com.example.ferias.data.hotel_manager.Hotel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Traveler extends User implements Serializable {

    ////////////////     BOOKINGS     ////////////////
    private List<String> bookings;

    public Traveler(){
        super();
    }

    public Traveler(String name, String surname, String phone, String email, boolean isGoogle) {
        super(name, surname, phone, email, isGoogle);
        bookings = new ArrayList<>();
    }

    public Traveler(String name, String surname, String email, String phone, String password) {
        super(name, surname, email, phone, password);
        bookings = new ArrayList<>();
    }

    //////////////// GETS BEGIN ////////////////
    public List<String> getBookings() {
        return bookings;
    }
    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void setBookings(List<String> bookings) {
        this.bookings = bookings;
    }

    public void addBooking(String booking) {
        if(bookings==null){
            bookings = new ArrayList<>();
        }
        this.bookings.add(booking);
    }

    public void removeBookingyIndex(int index) {
        this.bookings.remove(index);
    }

    //////////////// SETS END ////////////////


}
