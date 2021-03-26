package com.example.ferias.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ferias.R;
import com.example.ferias.data.hotel_manager.HotelManager;
import com.example.ferias.data.simple_user.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationOptions extends Fragment {
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    private TextView tv_type_user;

    private LinearLayout ll_NormalUser, ll_HotelManager;

    private Button bt_RegistrationAsUserWithEmail, bt_RegistrationAsUserWithGoogle;
    private Button bt_RegistrationAsManagerWithEmail, bt_RegistrationAsManagerWithGoogle;

    private ImageButton bt_Backhome_RegistrationOptions;

    private  boolean typeLogin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration_options, container, false);

        initializeElements(root);

        clickListener(root);

        return root;
    }

    private void initializeElements(View root) {
        tv_type_user = root.findViewById(R.id.tv_type_user);

        ll_NormalUser = root.findViewById(R.id.ll_NormalUser);
        bt_RegistrationAsUserWithEmail = root.findViewById(R.id.bt_RegistrationAsUserWithEmail);
        bt_RegistrationAsUserWithGoogle = root.findViewById(R.id.bt_RegistrationAsUserWithGoogle);

        ll_HotelManager = root.findViewById(R.id.ll_HotelManager);
        bt_RegistrationAsManagerWithEmail = root.findViewById(R.id.bt_RegistrationAsManagerWithEmail);
        bt_RegistrationAsManagerWithGoogle = root.findViewById(R.id.bt_RegistrationAsManagerWithGoogle);

        bt_Backhome_RegistrationOptions = root.findViewById(R.id.bt_Backhome_RegistrationOptions);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        typeLogin = sharedPref.getBoolean("Registration_Manager", false);

        if(typeLogin){
            tv_type_user.setText("Hotel Manager");
            ll_NormalUser.setVisibility(View.GONE);
            ll_HotelManager.setVisibility(View.VISIBLE);
        }
        else{
            tv_type_user.setText("Simple User");
            ll_NormalUser.setVisibility(View.VISIBLE);
            ll_HotelManager.setVisibility(View.GONE);
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("Registration_Manager");
        editor.commit();

    }

    private void clickListener(View root) {
        bt_Backhome_RegistrationOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_registrationOptions_to_loginOptions);
            }
        });

        bt_RegistrationAsUserWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_registrationOptions_to_registration_as_simpleuser);
            }
        });

        bt_RegistrationAsUserWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationwithGoogle();
            }
        });


        bt_RegistrationAsManagerWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_registrationOptions_to_registration_as_hotelmanager);
            }
        });

        bt_RegistrationAsManagerWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationwithGoogle();
            }
        });
    }

    private void registrationwithGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(),googleSignInOptions);

        Intent intent = googleSignInClient.getSignInIntent();

        startActivityForResult(intent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            Task task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = (GoogleSignInAccount) task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            if(typeLogin){
                                HotelManager newuser = new HotelManager();
                                newuser.setName(acct.getDisplayName());
                                newuser.setEmail(acct.getEmail());
                                newuser.setIsgoogle(true);

                                FirebaseDatabase.getInstance().getReference("HotelManager")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(newuser).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(getContext(),"User has ben registered successfully!", Toast.LENGTH_LONG).show();
                                        Toast.makeText(getContext(),"An email has been sent to activate your account!", Toast.LENGTH_LONG).show();

                                        final NavController navController = Navigation.findNavController(getView());
                                        navController.navigate(R.id.action_registration_as_simpleuser_to_loginOptions);
                                    }
                                    else {
                                        Toast.makeText(getContext(),"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else{
                                User newuser = new User();
                                newuser.setName(acct.getDisplayName());
                                newuser.setEmail(acct.getEmail());
                                newuser.setIsgoogle(true);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(newuser).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(getContext(),"User has ben registered successfully!", Toast.LENGTH_LONG).show();
                                        Toast.makeText(getContext(),"An email has been sent to activate your account!", Toast.LENGTH_LONG).show();

                                        final NavController navController = Navigation.findNavController(getView());
                                        navController.navigate(R.id.action_registration_as_simpleuser_to_loginOptions);
                                    }
                                    else {
                                        Toast.makeText(getContext(),"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(getContext(), "Sorry auth failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
