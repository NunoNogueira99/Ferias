package com.example.ferias.data.simple_user;

import com.example.ferias.data.common.Address;

import java.io.Serializable;

public class User implements Serializable {

    ////////////////   PERSONAL DATA    ////////////////
    private String name;
    private String birthday;
    private String phone;
    private Address address;
    ////////////////     SECURITY     ////////////////
    private String email;
    private String password;
    ////////////////    PREFERENCES    ////////////////
    private String language;
    private String units_distance;
    private String units_currency;
    private boolean isGoogle;
    //////////////// PAYMENT DETAILS ////////////////


    public User(){
        this.language = "EN";
        this.units_distance = "M";
        this.units_currency = "EURO";
    }

    public User(String name, String email, String phone, String password) {

        this.name = name;
        //////////////////////////////
        this.phone = phone;
        this.email = email;
        this.password = password;
        //////////////////////////////
        this.language = "EN";
        this.units_distance = "M";
        this.units_currency = "EURO";
    }

    public User(String name, String email, String birthday, String phone, String password, String language, String units_currency, String units_distance) {
        this.name = name;
        this.birthday = birthday;
        //////////////////////////////
        this.phone = phone;
        this.email = email;
        this.password = password;
        //////////////////////////////
        this.language = language;
        this.units_distance = units_distance;
        this.units_currency = units_currency;
    }

    public User(String name, String phone, String email, boolean isGoogle) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.isGoogle = isGoogle;
    }


    //////////////// GETS BEGIN ////////////////
    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress(){ return address;}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLanguage() {
        return language;
    }

    public String getUnitsDistance() {
        return units_distance;
    }

    public String getUnitsCurrency() {
        return units_currency;
    }

    public boolean isGoogle() {
        return isGoogle;
    }
    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(Address address){ this.address = address;}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setUnitsDistance(String units_distance) {
        this.units_distance = units_distance;
    }

    public void setUnitsCurrency(String units_currency) {
        this.units_currency = units_currency;
    }

    public void setGoogle(boolean google) {
        this.isGoogle = google;
    }
    //////////////// SETS END ////////////////

    @Override
    public String toString() {
        return "\n" +"User{"+ "\n" +
                "name='" + name + "\n" +
                ", birthday=" + birthday + "\n"+
                ", phone='" + phone + "\n" +
                ", email='" + email + "\n" +
                ", password='" + password + "\n" +
                ", language='" + language + "\n" +
                ", units_distance='" + units_distance + "\n" +
                ", units_currency='" + units_currency + "\n" +
                '}';
    }


}

