package com.example.ferias.data.simple_user;

import java.io.Serializable;
import java.util.Calendar;

public class User implements Serializable {

    private String name;
    private Calendar birthday;
    //////////////////////////////
    private String phone;
    private String email;
    private String password;
    //////////////////////////////
    private String language;
    private String units_distance;
    private String units_currency;

    public User(String name, String email, String phone, String password){
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

    public User(String name, String email, Calendar birthday, String phone, String password) {

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

    //////////////// GETS BEGIN ////////////////

    public String get_Name() {
        return name;
    }

    public Calendar get_Birthday() {
        return birthday;
    }

    public String get_Phone() {
        return phone;
    }

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

    public void set_Birthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public void set_Phone(String email) {
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
}

