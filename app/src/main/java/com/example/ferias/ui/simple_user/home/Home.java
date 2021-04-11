package com.example.ferias.ui.simple_user.home;

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
import com.example.ferias.data.simple_user.SimpleUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private SimpleUser user;

    private GoogleSignInClient googleSignInClient;

    private ConstraintLayout cl_HomeUser;

    private ShapeableImageView bt_ProfileMenu;
    private LinearLayout profileMenu;
    private Button bt_EditProfile, bt_Logout;

    private TextView tv_NameMensage;
    private MaterialButton search_btn;
    private TextView textinput_location;

    private ExtendedFloatingActionButton partyBtn;
    private ExtendedFloatingActionButton chillBtn;
    private ExtendedFloatingActionButton adventureBtn;
    private ExtendedFloatingActionButton sportsBtn;

    private FloatingActionButton favsBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.simple_user_fragment_home, container, false);

        readUserData();

        initializeElements(root);

        clickListener(root);

        loadDatatoElements();

        return root;
    }

    private void initializeElements(View root) {
        bt_ProfileMenu = root.findViewById(R.id.bt_ProfileMenu);

        profileMenu = root.findViewById(R.id.ll_profile_menu_User);
        profileMenu.setVisibility(View.GONE);

        bt_EditProfile = root.findViewById(R.id.bt_editProfile_User);

        bt_Logout = root.findViewById(R.id.bt_Logout_User);

        cl_HomeUser = root.findViewById(R.id.cl_Home_User);

        tv_NameMensage = root.findViewById(R.id.tv_NameMensage_User);

        search_btn = root.findViewById(R.id.home_search_btn);
        textinput_location= root.findViewById(R.id.textinput_location);

        partyBtn = root.findViewById(R.id.simple_userParty_search);;
        chillBtn = root.findViewById(R.id.simple_userChill_search);;
        adventureBtn = root.findViewById(R.id.simple_userAdventure_search);;
        sportsBtn = root.findViewById(R.id.simple_userSport_search);;

        favsBtn=root.findViewById(R.id.my_favs_btn2);
    }

    private void clickListener(View root) {
        cl_HomeUser.setOnClickListener(v -> profileMenu.setVisibility(View.GONE));

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
            navController.navigate(R.id.action_simple_user_home_to_profile);
        });

        bt_Logout.setOnClickListener(v -> {

            if(user.isGoogle()  == true){
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
            navController.navigate(R.id.action_simple_user_home_to_login);
        });

        search_btn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("inputText", textinput_location.getText().toString());
            Navigation.findNavController(root).navigate(R.id.action_simple_user_home_to_simple_user_search, bundle);
        });

        partyBtn.setOnClickListener(v -> {SearchByMods(root,"Party");});
        chillBtn.setOnClickListener(v -> {SearchByMods(root,"Chill");});
        adventureBtn.setOnClickListener(v -> {SearchByMods(root,"Adventure");});
        sportsBtn.setOnClickListener(v -> {SearchByMods(root,"Sports");});

        favsBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_simple_user_home_to_favorites);
        });
    }

    private void readUserData() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(SimpleUser.class);
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(SimpleUser.class);
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

    private void SearchByMods(View root,String mod)
    {
        Bundle bundle = new Bundle();
        bundle.putString("modsWanted", mod);
        Navigation.findNavController(root).navigate(R.id.action_simple_user_home_to_simple_user_search, bundle);
    }

}