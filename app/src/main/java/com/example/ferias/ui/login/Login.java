package com.example.ferias.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;

public class Login extends Fragment {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    private EditText et_EmailAddress,et_Password;
    private CheckBox cb_Remeber;
    private Button bt_Login;
    private ProgressBar progressBar_Login;

    private Button bt_Register_Email,bt_Register_Google;
    private TextView tv_Forgot_Password;

    public View main_root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        main_root = root;

        et_EmailAddress =  root.findViewById(R.id.etEmail_Login);
        et_Password =  root.findViewById(R.id.etPassword_Login);

        cb_Remeber = root.findViewById(R.id.cb_Remeber);

        bt_Login =  root.findViewById(R.id.bt_Login);

        progressBar_Login =  root.findViewById(R.id.progressBar_Login);
        progressBar_Login.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        tv_Forgot_Password =  root.findViewById(R.id.tv_Forgot_Password);

        bt_Register_Email =  root.findViewById(R.id.bt_Register_Email);

        bt_Register_Google =  root.findViewById(R.id.bt_Register_Google);

        clickListener(root);

        verifyLogin();

        return root;
    }

    private void clickListener(final View root) {
        bt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin(root);
            }
        });

        tv_Forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_navigation_home_to_forgotPassword);
            }
        });

        bt_Register_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerGoogle();
            }
        });

        bt_Register_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_navigation_home_to_registration);
            }
        });
    }

    private void registerGoogle() {
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
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            NavController navController = Navigation.findNavController(getView());
                            navController.navigate(R.id.action_navigation_home_to_simple_user_home);

                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("GoogleLogin", true);
                            editor.commit();

                        } else {
                            Toast.makeText(getContext(), "Sorry auth failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void verifyLogin() {
        try {
            String email = (String) InternalStorage.readObject(getContext(), "Email");
            String password = (String) InternalStorage.readObject(getContext(), "Password");

            if(!email.isEmpty() && !password.isEmpty()){
                et_EmailAddress.setText(email);
                et_Password.setText(password);

                cb_Remeber.setChecked(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void userLogin(View root) {
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

        if(password.length() < 6){
            et_Password.setError("Min password length should be 6 characters");
            et_Password.requestFocus();
            error = true;
        }

        if(error){
            return;
        }

        progressBar_Login.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(cb_Remeber.isChecked()){
                        try {
                            InternalStorage.writeObject(getContext(), "Email", email);
                            InternalStorage.writeObject(getContext(), "Password", password);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            InternalStorage.writeObject(getContext(), "Email", "");
                            InternalStorage.writeObject(getContext(), "Password", "");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(user.isEmailVerified()){
                        Toast.makeText(getContext(),"Login Successful",Toast.LENGTH_LONG).show();
                        progressBar_Login.setVisibility(View.GONE);

                        NavController navController = Navigation.findNavController(root);
                        navController.navigate(R.id.action_navigation_home_to_simple_user_home);
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(getContext(),"Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(getContext(),"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}