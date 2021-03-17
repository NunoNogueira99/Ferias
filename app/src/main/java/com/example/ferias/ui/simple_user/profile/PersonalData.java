package com.example.ferias.ui.simple_user.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ferias.R;
import com.example.ferias.data.InternalStorage;
import com.example.ferias.data.simple_user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class PersonalData extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private User user;

    private EditText et_FullName,et_Age,et_Phone,et_EmailAddress;
    private EditText et_Country,et_City,et_Address,et_ZipCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.simple_user_fragment_profile_personal_data, container, false);

        return root;
    }

    private void initializeElements(View root){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        et_FullName =  root.findViewById(R.id.et_FullName);
        et_Age =  root.findViewById(R.id.et_Age);
        et_Phone =  root.findViewById(R.id.et_Phone);
        et_EmailAddress =  root.findViewById(R.id.et_Email);

        et_Country =  root.findViewById(R.id.et_Country);
        et_City =  root.findViewById(R.id.et_City);
        et_Phone =  root.findViewById(R.id.et_Phone);
        et_Address =  root.findViewById(R.id.et_ZipCode);
    }

    private void clickListeners(View root) {

    }

    private void readUserData() {
        try {
            user = (User) InternalStorage.readObject(getContext(), "User");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadDatatoElements(){

    }

    private void savePersonalData(){
        databaseReference.setValue(user);
    }
}