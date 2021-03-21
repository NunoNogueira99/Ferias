package com.example.ferias.ui.hotel_manager.Registration;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ferias.R;
import com.example.ferias.data.simple_user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterAsAdmin extends Fragment {
    private ImageButton bt_Backhome_Registration;

    private FirebaseAuth firebaseAuth;

    private EditText et_FullName, et_Phone,et_Address, et_EmailAddress, et_Password;
    private Button bt_RegisterUserAdmin;
    private ProgressBar progressBar_Register;

    public View main_root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register_as_admin, container, false);
        main_root = root;
        firebaseAuth = FirebaseAuth.getInstance();

        initializeElements(root);
        clickListener(root);
        return root;
    }

    private void initializeElements(View root) {
        bt_RegisterUserAdmin = root.findViewById(R.id.bt_Registration_HotelManager);
        bt_Backhome_Registration = root.findViewById(R.id.bt_Backhome_Registration_HotelManager);

        et_FullName =  root.findViewById(R.id.et_FullName_Registration_HotelManager);

        et_Phone =  root.findViewById(R.id.et_Phone_Registration_HotelManager);

        et_Address =  root.findViewById(R.id.et_Address_Registration_HotelManager);

        et_EmailAddress =  root.findViewById(R.id.et_Email_Registration_HotelManager);

        et_Password =  root.findViewById(R.id.et_Password_Registration_HotelManager);

        progressBar_Register =  root.findViewById(R.id.progressBar_Registration_HotelManager);
        progressBar_Register.setVisibility(View.GONE);
    }
    private void clickListener(View root) {
        /*t_Backhome_Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_registerAsAdmin_to_registrationOptions);
            }
        });*/
        /*
        bt_RegisterUserAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_registerAsAdmin_to_home_main);
            }
        });
         */
    }

    private void registerUserAsAdmin(View root) {
        String companyName = et_FullName.getText().toString().trim();
        String phone = et_Phone.getText().toString().trim();
        String address = et_Address.getText().toString().trim();

        String email = et_EmailAddress.getText().toString().trim();
        String password = et_Password.getText().toString().trim();
        boolean error = false;

        if (companyName.isEmpty()) {
            et_FullName.setError("Full name is required");
            et_FullName.requestFocus();
            error = true;
        }

        if (phone.isEmpty()) {
            et_Phone.setError("Phone is required");
            et_Phone.requestFocus();
            error = true;
        }

        if (email.isEmpty()) {
            et_EmailAddress.setError("Email Address is required");
            et_EmailAddress.requestFocus();
            error = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_EmailAddress.setError("Please provide valid Email Address");
            et_EmailAddress.requestFocus();
            error = true;
        }

        if (password.isEmpty()) {
            et_Password.setError("Password is required");
            et_Password.requestFocus();
            error = true;
        }

        if (password.length() < 6) {
            et_Password.setError("Min password length should be 6 characters");
            et_Password.requestFocus();
            error = true;
        }

        if (error) {
            return;
        }

        progressBar_Register.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User newuser = new User(companyName, email, address, phone, password);

                        FirebaseDatabase.getInstance().getReference("Admin Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(newuser).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(getContext(), "Admin User has ben registered successfully!", Toast.LENGTH_LONG).show();
                                progressBar_Register.setVisibility(View.GONE);

                                Toast.makeText(getContext(), "An email has been sent to activate your account!", Toast.LENGTH_LONG).show();

                                final NavController navController = Navigation.findNavController(root);
                                navController.navigate(R.id.action_registration_to_navigation_home);
                            } else {
                                Toast.makeText(getContext(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar_Register.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        progressBar_Register.setVisibility(View.GONE);
                    }
                });
    }
}