package com.example.ferias.ui.hotel_manager.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ferias.R;
import com.example.ferias.data.InternalStorage;
import com.example.ferias.data.hotel_manager.HotelManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class Home extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private HotelManager user;

    private GoogleSignInClient googleSignInClient;

    private ConstraintLayout cl_HomeManager;

    private ShapeableImageView bt_ProfileMenu;
    private MaterialCardView bt_ManageHotels;

    private LinearLayout profileMenu;
    private Button bt_EditProfile, bt_Logout;

    private TextView tv_NameMensage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.hotel_manager_fragment_home, container, false);

        readUserData();

        initializeElements(root);

        clickListener(root);

        loadDatatoElements();

        return root;
    }

    private void initializeElements(View root) {
        cl_HomeManager = root.findViewById(R.id.cl_Home_Manager);

        bt_ProfileMenu = root.findViewById(R.id.bt_ProfileMenu_Manager);

        profileMenu = root.findViewById(R.id.ll_ProfileMenu_Manager);
        profileMenu.setVisibility(View.GONE);

        bt_EditProfile = root.findViewById(R.id.bt_EditProfile_Manager);

        bt_Logout = root.findViewById(R.id.bt_Logout_Manager);

        tv_NameMensage = root.findViewById(R.id.tv_NameMensage_Manager);

        bt_ManageHotels = root.findViewById(R.id.bt_ManageHotels);
    }

    private void clickListener(View root) {
        cl_HomeManager.setOnClickListener(v -> profileMenu.setVisibility(View.GONE));

        bt_ProfileMenu.setOnClickListener(v -> {
            if(profileMenu.getVisibility() == View.GONE){
                profileMenu.setVisibility(View.VISIBLE);
                profileMenu.bringToFront();
            }
            else{
                profileMenu.setVisibility(View.GONE);
            }

        });

        bt_EditProfile.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_hotel_manager_home_to_profile);
        });

        bt_ManageHotels.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_hotel_manager_home_to_hotel_registration);
        });

        bt_Logout.setOnClickListener(v -> {

            if(user.isGoogle() == true){
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            firebaseAuth.signOut();
                        }
                    }
                });
            }
            else{
                firebaseAuth.signOut();
            }

            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_hotel_manager_home_to_login);
        });
    }

    private void readUserData() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Hotel Manager").child(firebaseUser.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(HotelManager.class);
                Log.e("User",user.toString());
                if(user != null){
                    loadDatatoElements();

                    try {
                        InternalStorage.writeObject(getContext(), "User", user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "getUser:onCancelled",error.toException());
            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(HotelManager.class);
                if(user != null){
                    loadDatatoElements();

                    try {
                        InternalStorage.writeObject(getContext(), "User", user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "getUser:onCancelled",error.toException());
            }
        });
    }

    private void loadDatatoElements(){
        if(user != null){
            tv_NameMensage.setText("Hi "+user.getName());
        }
    }
}