package com.example.ferias.ui.hotel_manager.manage_hotels;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ferias.R;
import com.example.ferias.data.common.Address;
import com.example.ferias.data.hotel_manager.Hotel;
import com.example.ferias.data.hotel_manager.HotelFeature;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;


public class Hotel_Registration extends Fragment {
    private EditText et_Name, et_Phone, et_Description, et_TotalRooms, et_Price, et_City, et_Address, et_ZipCode;
    private CheckBox cb_Restaurant, cb_ServiceRooms, cb_Pub, cb_Breakfast, cb_Lunch, cb_Dinner, cb_Reception, cb_AirConditioner, cb_Wifi, cb_OutsidePool, cb_InsidePool, cb_Spa, cb_Sauna, cb_Gymnasium, cb_Garden;
    private ImageButton ib_Gallery;
    private RatingBar rb_Stars;
    private CountryCodePicker ccp_PhoneCode_Property, ccp_country;

    private Button bt_RegisterHotel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.hotel_manager_fragment_hotel_registration, container, false);

        initializeElements(root);

        clickListeners(root);

        return root;
    }

    private void clickListeners(final View root) {

        bt_RegisterHotel.setOnClickListener(v -> {
            verifyData();
        });
        //ib_Gallery.setOnClickListener();

    }

    private void initializeElements(View root) {


        et_Name = root.findViewById(R.id.et_PropertyName);
        ccp_PhoneCode_Property = root.findViewById(R.id.ccp_PhoneCode_Property);
        et_Phone=root.findViewById(R.id.et_Phone_Property);
        ccp_PhoneCode_Property.registerCarrierNumberEditText(et_Phone);
        et_Description = root.findViewById(R.id.et_Description);
        rb_Stars = root.findViewById(R.id.ratingBar_Hotel_Rating);
        rb_Stars.setNumStars(5);
        rb_Stars.setMax(5);
        rb_Stars.setStepSize(1);

        et_TotalRooms = root.findViewById(R.id.et_TotalRooms);
        et_Price = root.findViewById(R.id.et_PriceRooms);

        ccp_country = root.findViewById(R.id.ccp_Country);
        et_City = root.findViewById(R.id.et_City);
        et_Address = root.findViewById(R.id.et_Address);
        et_ZipCode = root.findViewById(R.id.et_ZipCode);


        //////////////HOTEL FEATURES///////////////

        cb_Restaurant = root.findViewById(R.id.Restaurant);
        cb_ServiceRooms = root.findViewById(R.id.ServiceRooms);
        cb_Pub = root.findViewById(R.id.Pub);

        cb_Lunch = root.findViewById(R.id.Lunch);
        cb_Dinner = root.findViewById(R.id.Dinner);
        cb_Breakfast = root.findViewById(R.id.Breakfast);

        cb_Reception = root.findViewById(R.id.Reception);
        cb_AirConditioner = root.findViewById(R.id.AirConditioner);
        cb_Wifi = root.findViewById(R.id.WiFiAccess);

        cb_OutsidePool = root.findViewById(R.id.Outside_pool);
        cb_InsidePool = root.findViewById(R.id.Inside_pool);
        cb_Spa = root.findViewById(R.id.Spa);

        cb_Sauna = root.findViewById(R.id.Sauna);
        cb_Gymnasium = root.findViewById(R.id.Gymnasium);
        cb_Garden = root.findViewById(R.id.Garden);


        ib_Gallery = root.findViewById(R.id.adminAddHotelPhotos);
        bt_RegisterHotel = root.findViewById(R.id.bt_RegisterHotel);

    }


    private void verifyData(){

        String name = et_Name.getText().toString().trim();

        if(name.isEmpty()){
            Toast.makeText(getContext(), "You did not enter a Hotel name", Toast.LENGTH_LONG).show();
            return;
        }

        float starts = rb_Stars.getRating();

        String phone = ccp_PhoneCode_Property.getFormattedFullNumber();

        int total_rooms = Integer.valueOf(et_TotalRooms.getText().toString());

        float price = Integer.valueOf(et_Price.getText().toString());

        //////////////ADDRESS/////////////////

        String country = ccp_country.getSelectedCountryName();

        String city = et_City.getText().toString().trim();

        if(city.isEmpty()){
            Toast.makeText(getContext(), "You did not enter a city", Toast.LENGTH_LONG).show();
            return;
        }

        String address_string = et_Address.getText().toString().trim();

        if(address_string.isEmpty()){
            Toast.makeText(getContext(), "You did not enter a address", Toast.LENGTH_LONG).show();
            return;
        }

        String zip_code = et_ZipCode.getText().toString().trim();

        if(zip_code.isEmpty()){
            Toast.makeText(getContext(), "You did not enter a city", Toast.LENGTH_LONG).show();
            return;
        }



        Address address = new Address(country, city, address_string, zip_code);

        //////////////////////////////////////

        //////////////FEATURES////////////////

        boolean restaurant=false;
        if(cb_Restaurant.isChecked()){
            restaurant=true;
        }

        boolean service_rooms=false;
        if(cb_ServiceRooms.isChecked()){
            service_rooms=true;
        }

        boolean pub=false;
        if(cb_Pub.isChecked()){
            pub=true;
        }
        //----------

        boolean lunch=false;
        if(cb_Lunch.isChecked()){
            lunch=true;
        }

        boolean dinner=false;
        if(cb_Dinner.isChecked()){
            dinner=true;
        }

        boolean breakfast=false;
        if(cb_Breakfast.isChecked()){
            breakfast=true;
        }
        //-----------

        boolean reception=false;
        if(cb_Reception.isChecked()){
            reception=true;
        }

        boolean air_conditioner=false;
        if(cb_AirConditioner.isChecked()){
            air_conditioner=true;
        }

        boolean wifi=false;
        if(cb_Wifi.isChecked()){
            wifi=true;
        }
        //------------

        boolean outside_pool=false;
        if(cb_OutsidePool.isChecked()){
            outside_pool=true;
        }

        boolean inside_pool=false;
        if(cb_InsidePool.isChecked()){
            inside_pool=true;
        }

        boolean spa=false;
        if(cb_Spa.isChecked()){
            spa=true;
        }
        //------------

        boolean sauna=false;
        if(cb_Sauna.isChecked()){
            sauna=true;
        }

        boolean gymnasium=false;
        if(cb_Gymnasium.isChecked()){
            gymnasium=true;
        }

        boolean garden=false;
        if(cb_Garden.isChecked()){
            garden=true;
        }

        HotelFeature hotel_feature = new HotelFeature(restaurant, service_rooms, pub, breakfast, lunch, dinner, reception, air_conditioner, wifi, outside_pool, inside_pool, spa, sauna, gymnasium, garden);
        //////////////////////////////////////

        String description = et_Description.getText().toString().trim();

        if(description.isEmpty()){
            Toast.makeText(getContext(), "You did not enter a description", Toast.LENGTH_LONG).show();
            return;
        }


        /////////////HOTEL MANAGER FROM FIREBASE/////////////////

        //Ler dados da base de dados
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        /////////////////////////////////////////////////////////


        //Não sei como fazer com o button para adicionar a foto do hotel

        Hotel hotel = new Hotel(name, phone, description, address, firebaseUser.getUid(), price, starts, total_rooms, "none", hotel_feature);

        registerOnFirebase(hotel);

    }


    private void registerOnFirebase(Hotel hotel){
        FirebaseDatabase.getInstance().getReference("Hotel")
                .child(String.valueOf(hotel.hashCode()))  //ALTERAR AQUI O ID
                .setValue(hotel).addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()){
                Toast.makeText(getContext(),"Hotel has ben registered successfully!", Toast.LENGTH_LONG).show();

                final NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_hotel_registration_to_hotel_manager_home);
            }
            else {
                Toast.makeText(getContext(),"Failed to register! Try again!", Toast.LENGTH_LONG).show();
            }
        });

    }
}