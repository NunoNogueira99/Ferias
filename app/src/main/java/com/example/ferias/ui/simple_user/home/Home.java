package com.example.ferias.ui.simple_user.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ferias.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Home extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private GoogleSignInClient googleSignInClient;

    private ConstraintLayout cl_Home;

    private ShapeableImageView bt_ProfileMenu;
    private LinearLayout profile_menu;
    private Button bt_editProfile, bt_Logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.simple_user_fragment_home, container, false);

        initializeElements(root);

        clickListener(root);

        return root;
    }

    private void initializeElements(View root) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        }
        bt_ProfileMenu = root.findViewById(R.id.bt_ProfileMenu);

        profile_menu = root.findViewById(R.id.ll_profile_menu);
        profile_menu.setVisibility(View.GONE);

        bt_editProfile = root.findViewById(R.id.bt_editProfile);

        bt_Logout = root.findViewById(R.id.bt_Logout);

         cl_Home = root.findViewById(R.id.cl_Home_User);
    }

    private void clickListener(View root) {
        cl_Home.setOnClickListener(v -> profile_menu.setVisibility(View.GONE));

        bt_ProfileMenu.setOnClickListener(v -> {
            if(profile_menu.getVisibility() == View.GONE){
                profile_menu.setVisibility(View.VISIBLE);
                profile_menu.bringToFront();
            }
            else{
                profile_menu.setVisibility(View.GONE);
            }

        });

        bt_editProfile.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_home_simple_user_to_profile_simpleuser);
        });

        bt_Logout.setOnClickListener(v -> {

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            boolean googleLogin = sharedPref.getBoolean("GoogleLogin", false);

            if(googleLogin == true){
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
            navController.navigate(R.id.action_home_simple_user_to_login);
        });
    }
}