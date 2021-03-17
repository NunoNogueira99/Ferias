package com.example.ferias.data.simple_user;

import java.io.Serializable;
import java.util.Calendar;

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
    //////////////// PAYMENT DETAILS ////////////////


    public User(){
    }

    public User(String name, String email, String birthday, String phone, String password) {

        this.name = name;
        this.birthday = birthday;
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

    //////////////// GETS BEGIN ////////////////
    public String get_Name() {
        return name;
    }

    public String get_Birthday() {
        return birthday;
    }

    public String get_Phone() {
        return phone;
    }

    public Address get_Address(){ return address;}

    public String get_Email() {
        return email;
    }

    public String get_Password() {
        return password;
    }

    public String get_Language() {
        return language;
    }

    public String get_Units_Distance() {
        return units_distance;
    }

    public String get_Units_Currency() {
        return units_currency;
    }
    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void set_Name(String name) {
        this.name = name;
    }

    public void set_Birthday(String birthday) {
        this.birthday = birthday;
    }

    public void set_Phone(String phone) {
        this.phone = phone;
    }

    public void set_Address(Address address){ this.address = address;}

    public void set_Email(String email) {
        this.email = email;
    }

    public void set_Password(String password) {
        this.password = password;
    }

    public void set_Language(String language) {
        this.language = language;
    }

    public void set_Units_Distance(String units_distance) {
        this.units_distance = units_distance;
    }

    public void set_Units_Currency(String units_currency) {
        this.units_currency = units_currency;
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

