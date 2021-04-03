package com.example.ferias.ui.hotel_manager.manage_hotels;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ferias.R;
import com.example.ferias.data.GenerateUniqueIds;
import com.example.ferias.data.common.Address;
import com.example.ferias.data.hotel_manager.Hotel;
import com.example.ferias.data.hotel_manager.HotelFeature;
import com.example.ferias.data.hotel_manager.HotelMoods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Map;


public class Hotel_Registration extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private EditText et_Name,  et_TotalRooms, et_Price;

    private TextView tv_Hotel_Stars ;
    private RatingBar rb_Stars;

    private CountryCodePicker ccp_PhoneCode;
    private EditText et_Phone;

    private CountryCodePicker ccp_country;
    private EditText et_City, et_Address, et_ZipCode;

    private ExtendedFloatingActionButton bt_Features;
    private TextView tv_Features_Selected;
    private HotelFeature hotelFeatures;

    private EditText et_Description;

    private TextView tv_Hotel_Moods_Title;
    private LinearLayout ll_Hotel_Moods;
    private HotelMoods hotelMoods;

    private MaterialButton bt_RegisterHotel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.hotel_manager_fragment_hotel_registration, container, false);

        initializeElements(root);

        clickListeners(root);

        return root;
    }

    private void initializeElements(View root) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        et_Name = root.findViewById(R.id.et_Hotel_Name);

        tv_Hotel_Stars = root.findViewById(R.id.tv_Hotel_Stars);
        rb_Stars = root.findViewById(R.id.rb_Hotel_Stars);

        ccp_PhoneCode = root.findViewById(R.id.ccp_PhoneCode_Hotel);
        et_Phone=root.findViewById(R.id.et_Phone_Hotel);
        ccp_PhoneCode.registerCarrierNumberEditText(et_Phone);

        et_TotalRooms = root.findViewById(R.id.et_Hotel_Total_Rooms);
        et_Price = root.findViewById(R.id.et_Hotel_Price_Rooms);

        ccp_country = root.findViewById(R.id.ccp_Hotel_Country);
        et_City = root.findViewById(R.id.et_Hotel_City);
        et_Address = root.findViewById(R.id.et_Hotel_Address);
        et_ZipCode = root.findViewById(R.id.et_Hotel_ZipCode);

        et_Description = root.findViewById(R.id.et_Description_Hotel);

        bt_Features = root.findViewById(R.id.bt_Features);
        tv_Features_Selected = root.findViewById(R.id.tv_Features_Selected);

        tv_Hotel_Moods_Title = root.findViewById(R.id.tv_Hotel_Moods_Title);
        ll_Hotel_Moods = root.findViewById(R.id.ll_Hotel_Moods);

        bt_RegisterHotel = root.findViewById(R.id.bt_RegisterHotel);

    }

    private void clickListeners(final View root) {

        bt_RegisterHotel.setOnClickListener(v -> {
            verifyData();
        });

        hotel_features_listern();

        hotel_moods_listern();
    }

    public void hotel_features_listern(){
        hotelFeatures = new HotelFeature(getResources().getStringArray(R.array.Features));

        String[] featuresKeys = new String[hotelFeatures.getHotelFeature()];
        boolean[] featuresValues = new boolean[hotelFeatures.getHotelFeature()];
        ArrayList<Integer> featuresSelected = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Boolean> entry : hotelFeatures.getFeatures().entrySet()){
            featuresKeys[index] = entry.getKey();
            featuresValues[index] = entry.getValue();
            index++;
        }

        bt_Features.setOnClickListener(view -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
            mBuilder.setTitle("Selected features of hotel");
            mBuilder.setMultiChoiceItems(featuresKeys, featuresValues, (dialogInterface, position, isChecked) -> {
                if(isChecked){
                    featuresSelected.add(position);
                    hotelFeatures.setFeatures_Value(featuresKeys[position],true);
                }else{
                    hotelFeatures.setFeatures_Value(featuresKeys[position],false);
                    featuresSelected.remove(position);
                }
            });

            mBuilder.setCancelable(false);
            mBuilder.setPositiveButton("Ok", (dialogInterface, which) -> {
                String item = "";
                for (int i = 0; i < featuresSelected.size(); i++) {
                    item = item + featuresKeys[featuresSelected.get(i)];
                    if (i != featuresSelected.size() - 1) {
                        item = item + ", ";
                    }
                }
                Log.e("Features", hotelFeatures.toString());
                tv_Features_Selected.setText(item);

            });

            mBuilder.setNegativeButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss());

            mBuilder.setNeutralButton("Clear all", (dialogInterface, which) -> {
                for (int i = 0; i < featuresValues.length; i++) {
                    featuresValues[i] = false;
                    featuresSelected.clear();
                }
                tv_Features_Selected.setText("");
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });
    }

    private void hotel_moods_listern() {
        ExtendedFloatingActionButton moods;
        hotelMoods = new HotelMoods(getResources().getStringArray(R.array.Moods), getResources().getStringArray(R.array.MoodsIcons));

        for (Map.Entry<String, Boolean> entry : hotelMoods.getMoods().entrySet()){
            moods = new ExtendedFloatingActionButton(getContext());

            moods.setId(entry.getKey().hashCode());
            moods.setText(entry.getKey());

            moods.setCheckable(true);
            moods.setChecked(entry.getValue());

            moods.setMaxLines(3);
            moods.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

            String drawable = hotelMoods.getMoods_Icon(entry.getKey());
            int id = getContext().getResources().getIdentifier(drawable, "drawable", getContext().getPackageName());
            moods.setIcon(ContextCompat.getDrawable(getContext(), id));
            moods.setIconTint(null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(30, 15, 30, 5);
            moods.setLayoutParams(params);

            ExtendedFloatingActionButton finalbutton = moods;
            finalbutton.setOnClickListener(v -> {
                if(finalbutton.isChecked()){
                    finalbutton.setChecked(true);
                    finalbutton.setBackgroundColor(Color.parseColor("#FF3F51B5"));
                    hotelMoods.setMoods_Value(entry.getKey(),true);
                }
                else{
                    finalbutton.setChecked(false);
                    finalbutton.setBackgroundColor(Color.parseColor("#15194A"));
                    hotelMoods.setMoods_Value(entry.getKey(),false);
                }
                Log.e("Moods", hotelMoods.toString());
            });

            ll_Hotel_Moods.addView(finalbutton);

        }
    }

    private void verifyData(){

        boolean error = false;
        ////////////// PERSONAL DATA /////////////////
        String name = et_Name.getText().toString().trim();
        float starts = rb_Stars.getRating();
        String phone = ccp_PhoneCode.getFormattedFullNumber();

        ////////////// ROOMS DATA /////////////////
        int total_rooms = Integer.parseInt(et_TotalRooms.getText().toString().trim());
        float price = Integer.parseInt(et_Price.getText().toString().trim());

        ////////////// ADDRESS /////////////////
        String country = ccp_country.getSelectedCountryNameCode();
        String city = et_City.getText().toString().trim();
        String address_string = et_Address.getText().toString().trim();
        String zip_code = et_ZipCode.getText().toString().trim();

        ////////////// DESCRIPTION /////////////////
        String description = et_Description.getText().toString().trim();


        if(name.isEmpty()){
            et_Name.setError("Hotel name is required");
            et_Name.requestFocus();
            error = true;
        }

        if(starts == 0){
            tv_Hotel_Stars.setError("Hotel stars is required");
            tv_Hotel_Stars.requestFocus();
            error = true;
        }

        if(!ccp_PhoneCode.isValidFullNumber()){
            et_Phone.setError("Phone is required or is not valid");
            et_Phone.requestFocus();
            error = true;
        }

        if(total_rooms <= 1){
            et_TotalRooms.setError("Total rooms is required and greater than 1");
            et_TotalRooms.requestFocus();
            error = true;
        }

        if(price <= 1){
            et_Price.setError("Price the rooms is required and greater than 1");
            et_Price.requestFocus();
            error = true;
        }

        if(city.isEmpty()){
            Toast.makeText(getContext(), "You did not enter a city", Toast.LENGTH_LONG).show();
            et_City.setError("City name is required");
            et_City.requestFocus();
            error = true;
        }

        if(address_string.isEmpty()){
            et_Address.setError("Address name is required");
            et_Address.requestFocus();
            error = true;
        }

        if(zip_code.isEmpty()){
            et_ZipCode.setError("Zip-Code is required");
            et_ZipCode.requestFocus();
            error = true;
        }

        if(tv_Features_Selected.getText().toString().trim().isEmpty()){
            tv_Features_Selected.setError("Select at least one feature");
            tv_Features_Selected.requestFocus();
            error = true;
        }

        if(description.isEmpty() || description.length() < 20){
            et_Description.setError("Description is required");
            et_Description.requestFocus();
            error = true;
        }

        if(!hotelMoods.getMoodsVerification()){
            tv_Hotel_Moods_Title.setError("Select at least one mood");
            tv_Hotel_Moods_Title.requestFocus();
            error = true;
        }

        if(error){
            return;
        }

        Address address = new Address(country, city, address_string, zip_code);

        Hotel hotel = new Hotel(name, phone, description, address, firebaseUser.getUid(), price, starts, total_rooms, hotelMoods, hotelFeatures);

        registerOnFirebase(hotel);

    }

    private void registerOnFirebase(Hotel hotel){
        FirebaseDatabase.getInstance().getReference("Hotel")
                .child(String.valueOf(GenerateUniqueIds.generateId()))  //ALTERAR AQUI O ID
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