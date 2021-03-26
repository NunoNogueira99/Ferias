package com.example.ferias.ui.simple_user.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ferias.R;
import com.example.ferias.data.InternalStorage;
import com.example.ferias.data.common.Address;
import com.example.ferias.data.simple_user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.Calendar;

public class PersonalData extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private User user;

    private EditText et_FullName,et_Age,et_EmailAddress;
    private EditText et_City,et_Address,et_ZipCode;

    private CountryCodePicker ccp_PhoneCode;
    private EditText et_Phone;

    private CountryCodePicker ccp_Country;

    private Button bt_save_preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.simple_user_fragment_profile_personal_data, container, false);

        initializeElements(root);

        readUserData();

        clickListeners();
        return root;
    }

    private void initializeElements(View root){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        et_FullName =  root.findViewById(R.id.et_FullName);
        et_Age =  root.findViewById(R.id.et_Age);
        et_EmailAddress =  root.findViewById(R.id.et_Email);

        ccp_PhoneCode = root.findViewById(R.id.ccp_PhoneCode);
        et_Phone =  root.findViewById(R.id.et_Phone);
        ccp_PhoneCode.registerCarrierNumberEditText(et_Phone);

        ccp_Country =  root.findViewById(R.id.ccp_Country);

        et_City =  root.findViewById(R.id.et_City);
        et_Address =  root.findViewById(R.id.et_Address);
        et_ZipCode =  root.findViewById(R.id.et_ZipCode);

        bt_save_preferences = root.findViewById(R.id.bt_save_preferences);
    }

    private void clickListeners() {
        et_Age.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final DatePickerDialog[] picker = new DatePickerDialog[1];
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker[0] = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar currentdate = Calendar.getInstance();
                                Calendar birthdaydate = Calendar.getInstance();
                                birthdaydate.set(year, monthOfYear, dayOfMonth);

                                int age = currentdate.get(Calendar.YEAR) - birthdaydate.get(Calendar.YEAR);
                                if (currentdate.get(Calendar.DAY_OF_YEAR) < birthdaydate.get(Calendar.DAY_OF_YEAR)) {
                                    age--;
                                }

                                if (age < 18) {
                                    Toast.makeText(getContext(), "Invalid birth date, must be over 18 years old. Please enter a valid date", Toast.LENGTH_LONG).show();
                                    et_Age.setText("");
                                } else {
                                    et_Age.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }
                        }, year, month, day);
                picker[0].show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_save_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePersonalData();
            }
        });

    }

    private void readUserData() {
        try {
            user = (User) InternalStorage.readObject(getContext(), "User");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        loadDatatoElements();
    }

    private void loadDatatoElements(){
        et_FullName.setText(user.getName());
        et_Age.setText(user.getBirthday());
        ccp_PhoneCode.setFullNumber(user.getPhone());
        et_EmailAddress.setText(user.getEmail());

        if(user.getAddress() != null){
            //ccp_Country.setDefaultCountryUsingNameCode(user.get_Address().getCountry());
            et_City.setText(user.getAddress().getAddress());
            et_Address.setText(user.getAddress().getAddress());
            et_ZipCode.setText(user.getAddress().getZipcode());
        }

    }

    private void savePersonalData(){
        String name = et_FullName.getText().toString().trim();
        String age = et_Age.getText().toString().trim();
        String phone = ccp_PhoneCode.getFormattedFullNumber();
        String email = et_EmailAddress.getText().toString().trim();

        String country = ccp_Country.getSelectedCountryName();
        String city = et_City.getText().toString().trim();
        String address = et_Address.getText().toString().trim();
        String zipcode = et_ZipCode.getText().toString().trim();

        boolean error = false;

        if(name.isEmpty()){
            et_FullName.setError("Full name is required");
            et_FullName.requestFocus();
            error = true;
        }

        if(age.isEmpty()){
            et_Age.setError("Age is required");
            et_Age.requestFocus();
            error = true;
        }

        if(!ccp_PhoneCode.isValidFullNumber()){
            et_Phone.setError("Phone is required and valid");
            et_Phone.requestFocus();
            error = true;
        }

        if(email.isEmpty()){
            et_EmailAddress.setError("Email Address is required");
            et_EmailAddress.requestFocus();
            error = true;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_EmailAddress.setError("Please provide valid Email Address");
            et_EmailAddress.requestFocus();
            error = true;
        }

        if(country.isEmpty()){
            error = true;
        }

        if(city.isEmpty()){
            et_City.setError("City is required");
            et_City.requestFocus();
            error = true;
        }

        if(address.isEmpty()){
            et_Address.setError("Address is required");
            et_Address.requestFocus();
            error = true;
        }

        if(zipcode.isEmpty()){
            et_ZipCode.setError("Zip-Code is required");
            et_ZipCode.requestFocus();
            error = true;
        }


        if(error){
            return;
        }

        user.setName(name);
        user.setBirthday(age);
        user.setPhone(phone);
        user.setEmail(email);

        Address newaddress = new Address();
        newaddress.setCountry(country);
        newaddress.setCity(city);
        newaddress.setAddress(address);
        newaddress.setZipcode(zipcode);

        user.setAddress(newaddress);

        databaseReference.setValue(user);
    }
}