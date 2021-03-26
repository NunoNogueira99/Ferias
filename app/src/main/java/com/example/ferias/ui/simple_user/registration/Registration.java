package com.example.ferias.ui.simple_user.registration;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ferias.R;
import com.example.ferias.data.PasswordStrength;
import com.example.ferias.data.simple_user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;


public class Registration extends Fragment {

    private FirebaseAuth firebaseAuth;

    private ImageButton bt_Backhome_Registration;

    private EditText et_FullName,et_Age,et_EmailAddress;
    private Button bt_RegisterUser;
    private ProgressBar progressBar_Register;

    private CountryCodePicker ccp_PhoneCode;
    private EditText et_Phone;

    private EditText et_Password;
    private ImageView bt_password_view;

    private TextView tv_passwordstrength_registration;
    private ProgressBar progressBar_passwordstrength_registration;

    private Calendar birthday;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.simple_user_fragment_registration, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        initializeElements(root);

        clickListeners(root);

        return root;
    }

    private void initializeElements(View root) {
        bt_Backhome_Registration =  root.findViewById(R.id.bt_Backhome_Registration);

        bt_RegisterUser =  root.findViewById(R.id.bt_RegistrationUser);

        et_FullName =  root.findViewById(R.id.et_FullName_Registration);

        et_Age =  root.findViewById(R.id.et_Age_Registration);

        ccp_PhoneCode = root.findViewById(R.id.ccp_PhoneCode_Registration);
        et_Phone =  root.findViewById(R.id.et_Phone_Registration);
        ccp_PhoneCode.registerCarrierNumberEditText(et_Phone);

        et_EmailAddress =  root.findViewById(R.id.et_Email_Registration);

        et_Password = root.findViewById(R.id.et_Password_Registration);
        bt_password_view = root.findViewById(R.id.bt_PasswordView_Registration);

        progressBar_passwordstrength_registration = root.findViewById(R.id.progressBar_PasswordStrength_Registration);
        tv_passwordstrength_registration = root.findViewById(R.id.tv_PasswordStrength_Registration);

        progressBar_Register =  root.findViewById(R.id.progressBar_RegistrationUser);
        progressBar_Register.setVisibility(View.GONE);
    }

    private void clickListeners(final View root) {
        bt_Backhome_Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_registration_as_hotelmanager_to_registrationOptions);
            }
        });

        bt_RegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(root);
            }
        });

        et_Age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    birthday = birthdaydate;
                                }
                            }
                        }, year, month, day);
                picker[0].show();
            }
        });

        bt_password_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_Password.getTransformationMethod() == null){
                    et_Password.setTransformationMethod(new PasswordTransformationMethod());
                    bt_password_view.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                }
                else {
                    et_Password.setTransformationMethod(null);
                    bt_password_view.setImageResource(R.drawable.ic_baseline_visibility_24);
                }
            }
        });

        et_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculatePasswordStrength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void registerUser(View root) {
        String name = et_FullName.getText().toString().trim();
        String age = birthday.toString().trim();
        String phone = ccp_PhoneCode.getFormattedFullNumber();
        String email = et_EmailAddress.getText().toString().trim();
        String password = et_Password.getText().toString().trim();
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

        PasswordStrength passwordStrength = PasswordStrength.calculate(password);
        if(password.isEmpty() || passwordStrength.strength <= 1){
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Password Strength Error");
            String mensage = "Your password needs to:" +
                    "\n\tInclude both lower and upper case characters" +
                    "\n\tInclude at least one number and symbol" +
                    "\n\tBe at least 8 characters long";

            dialog.setMessage(mensage);
            dialog.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
            error = true;
        }

        if(error){
            return;
        }

        progressBar_Register.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        User newuser = new User(name, email, birthday.toString(), phone, password);

                        FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(newuser).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(getContext(),"User has ben registered successfully!", Toast.LENGTH_LONG).show();
                                progressBar_Register.setVisibility(View.GONE);

                                Toast.makeText(getContext(),"An email has been sent to activate your account!", Toast.LENGTH_LONG).show();

                                final NavController navController = Navigation.findNavController(root);
                                navController.navigate(R.id.action_registration_as_simpleuser_to_loginOptions);
                            }
                            else {
                                Toast.makeText(getContext(),"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                progressBar_Register.setVisibility(View.GONE);
                            }
                        });
                    }
                    else {
                        Toast.makeText(getContext(),"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        progressBar_Register.setVisibility(View.GONE);
                    }
                });
    }

    private void calculatePasswordStrength(String str) {
        // Now, we need to define a PasswordStrength enum
        // with a calculate static method returning the password strength
        PasswordStrength passwordStrength = PasswordStrength.calculate(str);

        progressBar_passwordstrength_registration.setProgressTintList(ColorStateList.valueOf(passwordStrength.color));
        progressBar_passwordstrength_registration.setProgress(passwordStrength.strength);
        tv_passwordstrength_registration.setText(passwordStrength.msg);

    }
}