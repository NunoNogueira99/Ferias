package com.example.ferias.data.hotel_manager;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HotelMoods implements Serializable {

    private Map<String, Boolean> moods;
    private Map<String, String> moods_icons;

    public HotelMoods() {
        moods = new HashMap<>();
        moods_icons = new HashMap<>();
    }

    public HotelMoods(String[] names, String[] icons)
    {
        moods = new HashMap<>();
        moods_icons = new HashMap<>();

        if(names.length == icons.length){
            for(String name : names) {
                moods.put(name,false);
            }

            int i = 0;
            for(String icon : icons) {

                moods_icons.put(names[i],icon);
                i++;
            }
        }

        this.toString();
    }

    //////////////// GETS BEGIN ////////////////
    public Map<String, Boolean> getMoods() {
        return moods;
    }

    public Map<String, String> getMoods_Icons() {
        return moods_icons;
    }

    public String getMoods_Icon(String key) {
        return moods_icons.get(key);
    }

    public boolean getMoods_Values(String key) {
        return moods.get(key);
    }

    public boolean getMoodsVerification(){
        boolean select = false;
        for (Map.Entry<String, Boolean> entry : moods.entrySet()){
            if(entry.getValue()){
               select = true;
            }
        }
        return select;
    }
    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void setMoods(Map<String, Boolean> moods) {
        this.moods = moods;
    }

    public void setMoods_Icons(Map<String, String> moods_icons) {
        this.moods_icons = moods_icons;
    }

    public void setMoods_Value(String key, boolean value) {
        moods.put(key, value);
    }
    //////////////// SETS END ////////////////


    @Override
    public String toString() {
        String data = "Hotel Moods{" + '\n';
        for (Map.Entry<String, Boolean> entry : moods.entrySet()) {
            data += "{" + entry.getKey() + " : " + entry.getValue() + "}" + '\n';
        }
        data += '}';

        return data;
    }

}
