package com.example.ferias.data.simple_user;

import java.io.Serializable;

public class Address implements Serializable {

    private String userID;
    private String country;
    private String city;
    private String address;
    private String zipcode;

    public Address(){

    }

    public Address(String userID, String country, String city, String address, String zipcode) {
        this.userID = userID;
        this.country = country;
        this.city = city;
        this.address = address;
        this.zipcode = zipcode;
    }

    //////////////// GETS BEGIN ////////////////
    public String getUserID() {
        return userID;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getZipcode() {
        return zipcode;
    }

    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    //////////////// SETS END ////////////////


    @Override
    public String toString() {
        return "Address{" +
                "\n\tUserID='" + userID +
                "\n\tCountry='" + country +
                "\n\tCity='" + city +
                "\n\tAddress='" + address +
                "\n\tZip-Code='" + zipcode +
                '}';
    }
}
