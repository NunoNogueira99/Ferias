package com.example.ferias.data.hotel_manager;

import java.io.Serializable;

public class HotelFeature  implements Serializable {

    ///////////DATA/////////////
    private boolean restaurant;
    private boolean service_room;
    private boolean pub;

    private boolean breakfast;
    private boolean lunch;
    private boolean dinner;

    private boolean reception;
    private boolean air_conditioner;
    private boolean wifi;

    private boolean outside_pool;
    private boolean inside_pool;
    private boolean spa;

    private boolean sauna;
    private boolean gymnasium;
    private boolean garden;


    public HotelFeature(boolean restaurant, boolean service_room, boolean pub, boolean breakfast, boolean lunch, boolean dinner, boolean reception, boolean air_conditioner, boolean wifi, boolean outside_pool, boolean inside_pool, boolean spa, boolean sauna, boolean gymnasium, boolean garden) {

        this.restaurant = restaurant;
        this.service_room = service_room;
        this.pub = pub;

        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;

        this.reception = reception;
        this.air_conditioner = air_conditioner;
        this.wifi = wifi;

        this.outside_pool = outside_pool;
        this.inside_pool = inside_pool;
        this.spa = spa;

        this.sauna = sauna;
        this.gymnasium = gymnasium;
        this.garden = garden;
    }

    //////////////// GETS BEGIN ////////////////

    public boolean getRestaurant() { return restaurant; }

    public boolean getService_room() { return service_room; }

    public boolean getPub() { return pub; }

    public boolean getBreakfast() {
        return breakfast;
    }

    public boolean getLunch() {
        return lunch;
    }

    public boolean getDinner() {
        return dinner;
    }

    public boolean getReception() {
        return reception;
    }

    public boolean getAirConditioner() {
        return air_conditioner;
    }

    public boolean getWifi() {
        return wifi;
    }

    public boolean getOutside_pool() {
        return outside_pool;
    }

    public boolean getInside_pool() {
        return inside_pool;
    }

    public boolean getSpa() { return spa; }

    public boolean getSauna() { return sauna; }

    public boolean getGymnasium() { return gymnasium; }

    public boolean getGarden() { return garden; }

    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////

    public void setRestaurant(boolean restaurant) { this.restaurant = restaurant; }

    public void setService_room(boolean service_room) { this.service_room = service_room; }

    public void setPub(boolean pub) { this.pub = pub; }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public void setLunch(boolean lunch) {
        this.lunch = lunch;
    }

    public void setDinner(boolean dinner) {
        this.dinner = dinner;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public void setOutside_pool(boolean outside_pool) {
        this.outside_pool = outside_pool;
    }

    public void setInside_pool(boolean inside_pool) {
        this.inside_pool = inside_pool;
    }

    public void setSpa(boolean spa) { this.spa = spa; }

    public void setSauna(boolean sauna) { this.sauna = sauna; }

    public void setGymnasium(boolean gymnasium) { this.gymnasium = gymnasium; }

    public void setGarden(boolean garden) { this.garden = garden; }

    //////////////// SETS END ////////////////


}