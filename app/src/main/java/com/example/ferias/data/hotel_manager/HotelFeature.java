package com.example.ferias.data.hotel_manager;

import android.content.res.Resources;
import android.widget.CheckBox;

import com.example.ferias.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelFeature  implements Serializable {

    private Map<String, Boolean> features;

    public HotelFeature() {
        features = new HashMap<>();
    }

    public HotelFeature(String[] features_name) {
        features = new HashMap<>();

        for(String name : features_name) {
            features.put(name,false);
        }

    }

    //////////////// GETS BEGIN ////////////////
    public int getHotelFeature(){ return features.size();}

    public Map<String, Boolean> getFeatures() {
        return features;
    }

    public boolean getFeatures_Value(String key) {
        return features.get(key);
    }

    //////////////// GETS END ////////////////

    //////////////// SETS BEGIN ////////////////
    public void setFeatures(Map<String, Boolean> features) {
        this.features = features;
    }

    public void setFeatures_Value(String key, boolean value) {
        features.put(key, value);
    }
    //////////////// SETS END ////////////////


    @Override
    public String toString() {
        String data = "HotelFeature{" + '\n';
        for (Map.Entry<String, Boolean> entry : features.entrySet()) {
            data += "{" + entry.getKey() + " : " + entry.getValue() + "}" + '\n';
        }
        data += '}';

        return data;
    }

}