package com.example.ferias.ui.common.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ferias.R;
import com.example.ferias.data.InternalStorage;
import com.example.ferias.data.hotel_manager.HotelManager;
import com.example.ferias.data.simple_user.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class Login extends Fragment {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private User user;
    private HotelManager manager;

    private EditText et_EmailAddress,et_Password;

    private CheckBox cb_Remeber;

    private Button bt_Login, bt_Register;

    private ProgressBar progressBar_Login;

    private Button bt_Login_Google;
    private TextView tv_Forgot_Password;

    private boolean autoLogin = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        initializeElements(root);

        clickListener(root);

        getRemeberData();

        return root;
    }

    private void initializeElements(View root) {
        et_EmailAddress =  root.findViewById(R.id.etEmail_Login);
        et_Password =  root.findViewById(R.id.etPassword_Login);

        cb_Remeber = root.findViewById(R.id.cb_Remeber);

        bt_Login =  root.findViewById(R.id.bt_Login);

        progressBar_Login =  root.findViewById(R.id.progressBar_Login);
        progressBar_Login.setVisibility(View.GONE);

        tv_Forgot_Password =  root.findViewById(R.id.tv_Forgot_Password);

        bt_Login_Google =  root.findViewById(R.id.bt_Login_Google);

        bt_Register =  root.findViewById(R.id.bt_Register);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void clickListener(View root) {
        bt_Login.setOnClickListener(v -> verifyData(root));

        bt_Login_Google.setOnClickListener(v -> loginWithGoogle());

        bt_Register.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_login_to_registration);
        });

        tv_Forgot_Password.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_login_to_forgotPassword);
        });
    }

    private void getRemeberData() {
        try {
            String email = (String) InternalStorage.readObject(getContext(), "Email");
            String password = (String) InternalStorage.readObject(getContext(), "Password");

            if(!email.isEmpty() && !password.isEmpty()){
                et_EmailAddress.setText(email);
                et_Password.setText(password);

                cb_Remeber.setChecked(true);

                /*if(autoLogin){
                    bt_Login.performClick();
                }*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void loginWithGoogle() {
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
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information

                if(!verifyTypeUser(getView(),firebaseAuth.getCurrentUser().getUid())){
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("IsGoogle", true);
                    editor.commit();

                    NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_login_to_registration);
                }
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithCredential:failure", task.getException());
            }
        });
    }


    private void verifyData(View root){
        String email = et_EmailAddress.getText().toString().trim();
        String password = et_Password.getText().toString().trim();
        boolean error = false;

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

        if(password.isEmpty()){
            et_Password.setError("Password is required");
            et_Password.requestFocus();
            error = true;
        }

        if(error){
            return;
        }

        progressBar_Login.setVisibility(View.VISIBLE);

        login(root,email,password);
    }

    private void login(View root, String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(cb_Remeber.isChecked()){
                    saveData(email,password);
                }
                else{
                    saveData("","");
                }

                if(user.isEmailVerified()){
                    Toast.makeText(getContext(),"Login Successful",Toast.LENGTH_LONG).show();
                    progressBar_Login.setVisibility(View.GONE);

                    verifyTypeUser(root,user.getUid());
                }
                else{
                    user.sendEmailVerification();
                    Toast.makeText(getContext(),"Check your email to verify your account!", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getContext(),"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean verifyTypeUser(View root, String userId){

        NavController navController = Navigation.findNavController(root);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        DatabaseReference databaseReferenceManager = FirebaseDatabase.getInstance().getReference().child("Hotel Manager").child(firebaseUser.getUid());

        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "getUser:onCancelled",error.toException());
            }
        });

        databaseReferenceManager.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                manager = snapshot.getValue(HotelManager.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "getUser:onCancelled",error.toException());
            }
        });


        if(user != null){
            //editor.putBoolean("RegisterType", true); //RegisterType -> True = Normal User || False = Hotel Manager
            navController.navigate(R.id.action_login_to_home_simple_user);
            return true;
        }

        if(manager != null){
            //editor.putBoolean("RegisterType", true); //RegisterType -> True = Normal User || False = Hotel Manager
            navController.navigate(R.id.action_login_to_hotel_manager_home);
            return true;
        }

        editor.commit();

        return false;
    }

    private void saveData(String email, String password){
        try {
            InternalStorage.writeObject(getContext(), "Email", email);
            InternalStorage.writeObject(getContext(), "Password", password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}