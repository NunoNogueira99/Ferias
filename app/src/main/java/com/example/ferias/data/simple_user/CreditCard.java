package com.example.ferias.data.simple_user;

import java.io.Serializable;
import java.util.Calendar;

public class CreditCard implements Serializable {

    private int userID;
    private String name;
    //////////////////////////////
    private String number;
    private String date;
    private String cvc;

    public CreditCard(int userID, String name,String number,String date,String cvc){
        this.userID = userID;
        this.name = name;
        this.number = number;
        this.date = date;
        this.cvc = cvc;
    }

    //////////////// GETS BEGIN ////////////////
    public int get_UserID() {
        return userID;
    }

    public String get_Name() {
        return name;
    }

    public String get_Number() {
        return number;
    }

    public String get_Date() {
        return date;
    }

    public String get_CVC() {
        return cvc;
    }
    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void set_UserID(int userID) {
        this.userID = userID;
    }

    public void set_Name(String name) {
        this.name = name;
    }

    public void set_Number(String number) {
        this.number = number;
    }

    public void set_Date(String date) {
        this.date = date;
    }

    public void set_CVC(String cvc) {
        this.cvc = cvc;
    }
    //////////////// SETS END ////////////////
}
