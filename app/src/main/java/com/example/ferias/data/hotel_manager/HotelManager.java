package com.example.ferias.data.hotel_manager;

import com.example.ferias.data.Hotel;
import com.example.ferias.data.common.Address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HotelManager implements Serializable {

    ////////////////   PERSONAL DATA    ////////////////
    private String name;
    private String phone;
    private Address address;
    ////////////////     SECURITY     ////////////////
    private String email;
    private String password;
    ////////////////    PREFERENCES    ////////////////
    private boolean isgoogle;
    ////////////////     HOTELS     ////////////////
    private List<Hotel> hotels;

    public HotelManager(){
        this.isgoogle = false;
        this.hotels = new ArrayList<>();
    }

    public HotelManager(String name, String phone, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.isgoogle = false;
        this.hotels = new ArrayList<>();
    }

    //////////////// GETS BEGIN ////////////////
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIsgoogle() {
        return isgoogle;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }
    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsgoogle(boolean isgoogle) {
        this.isgoogle = isgoogle;
    }

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

    @Override
    public String toString() {
        return "HotelManager{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
