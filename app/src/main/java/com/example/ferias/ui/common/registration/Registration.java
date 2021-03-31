package com.example.ferias.ui.common.registration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ferias.R;
import com.example.ferias.data.PasswordStrength;
import com.example.ferias.data.hotel_manager.HotelManager;
import com.example.ferias.data.common.User;
import com.example.ferias.data.simple_user.SimpleUser;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;


public class Registration extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Button bt_RegisterUser;
    private ImageButton bt_Backhome_Registration;
    private ProgressBar progressBar_Register;

    private TextInputLayout tilCompanyName, tilFullName;
    private EditText et_FullName, et_CompanyName;

    private RadioButton rb_User, rb_Manager;

    private CountryCodePicker ccp_PhoneCode;
    private EditText et_Phone;

    private TextInputLayout tilEmail_Registration;
    private EditText et_EmailAddress;

    private LinearLayout ll_Password;
    private EditText et_Password;
    private ImageView bt_password_view;
    private TextView tv_passwordstrength_registration;
    private ProgressBar progressBar_passwordstrength_registration;

    private View mainRoot;
    private boolean googleRegistration;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        mainRoot = root;

        initializeElements(root);

        clickListeners(root);

        return root;
    }

    private void initializeElements(View root) {
        bt_Backhome_Registration =  root.findViewById(R.id.bt_Backhome_Registration);

        rb_User = root.findViewById(R.id.rb_User);
        rb_User.setChecked(true);

        rb_Manager = root.findViewById(R.id.rb_Manager);

        tilCompanyName = root.findViewById(R.id.tilCompanyName);
        et_CompanyName = root.findViewById(R.id.et_CompanyName_Registration);

        tilFullName = root.findViewById(R.id.tilFullName);
        et_FullName =  root.findViewById(R.id.et_FullName_Registration);

        ccp_PhoneCode = root.findViewById(R.id.ccp_PhoneCode_Registration);
        et_Phone =  root.findViewById(R.id.et_Phone_Registration);
        ccp_PhoneCode.registerCarrierNumberEditText(et_Phone);

        tilEmail_Registration = root.findViewById(R.id.tilEmail_Registration);
        et_EmailAddress = root.findViewById(R.id.et_Email_Registration);

        ll_Password  = root.findViewById(R.id.ll_Password);
        et_Password = root.findViewById(R.id.et_Password_Registration);
        bt_password_view = root.findViewById(R.id.bt_PasswordView_Registration);

        progressBar_passwordstrength_registration = root.findViewById(R.id.progressBar_PasswordStrength_Registration);
        tv_passwordstrength_registration = root.findViewById(R.id.tv_PasswordStrength_Registration);

        progressBar_Register =  root.findViewById(R.id.progressBar_RegistrationUser);
        progressBar_Register.setVisibility(View.GONE);

        bt_RegisterUser =  root.findViewById(R.id.bt_RegistrationUser);

        verifyIsGoogle();
    }

    private void verifyIsGoogle() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        googleRegistration = sharedPref.getBoolean("IsGoogle", false);

        if(googleRegistration){
            tilEmail_Registration.setVisibility(View.GONE);
            ll_Password.setVisibility(View.GONE);
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("IsGoogle");
        editor.commit();
    }

    private void clickListeners(final View root) {
        bt_Backhome_Registration.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_registration_to_login);
        });

        bt_RegisterUser.setOnClickListener(v -> verifyData());

        bt_password_view.setOnClickListener(v -> {
            if(et_Password.getTransformationMethod() == null){
                et_Password.setTransformationMethod(new PasswordTransformationMethod());
                bt_password_view.setImageResource(R.drawable.ic_baseline_visibility_off_24);
            }
            else {
                et_Password.setTransformationMethod(null);
                bt_password_view.setImageResource(R.drawable.ic_baseline_visibility_24);
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

        rb_User.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tilFullName.setVisibility(View.GONE);
            tilCompanyName.setVisibility(View.VISIBLE);
        });

        rb_Manager.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tilFullName.setVisibility(View.VISIBLE);
            tilCompanyName.setVisibility(View.GONE);
        });
    }

    private void verifyData(){
        String name = "";

        if(rb_Manager.isChecked()){
            name = et_CompanyName.getText().toString().trim();
        }

        if(rb_User.isChecked()){
            name = et_FullName.getText().toString().trim();
        }

        String phone = ccp_PhoneCode.getFormattedFullNumber();
        String email = et_EmailAddress.getText().toString().trim();
        String password = et_Password.getText().toString().trim();
        boolean error = false;

        if(name.isEmpty()){
            if(rb_Manager.isChecked()){
                et_CompanyName.setError("Full name is required");
                et_CompanyName.requestFocus();
            }

            if(rb_User.isChecked()){
                et_FullName.setError("Full name is required");
                et_FullName.requestFocus();
            }

            error = true;
        }

        if(!ccp_PhoneCode.isValidFullNumber()){
            et_Phone.setError("Phone is required and valid");
            et_Phone.requestFocus();
            error = true;
        }

        if(!googleRegistration){
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
            if(password.isEmpty() || passwordStrength.getStrength() <= 1){
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

        }
        else{
            email = firebaseAuth.getCurrentUser().getEmail();
        }

        if(error){
            return;
        }

        registerUser(name,phone,email,password);
    }

    private void registerUser(String name, String phone, String email, String password) {
        progressBar_Register.setVisibility(View.VISIBLE);
        String userId = null;
        if(!googleRegistration){
            firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    firebaseUser = firebaseAuth.getCurrentUser();
                    Toast.makeText(getContext(),"An email has been sent to activate your account!", Toast.LENGTH_LONG).show();
                    firebaseUser.sendEmailVerification();
                    registerInFirebase(name,phone,email,password,firebaseUser.getUid());
                }
                else {
                    Toast.makeText(getContext(),"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar_Register.setVisibility(View.GONE);
                    return;
                }
            });
        }
        else {
            firebaseUser = firebaseAuth.getCurrentUser();
            registerInFirebase(name,phone,email,password,firebaseUser.getUid());
        }

    }

    private void registerInFirebase(String name, String phone, String email, String password, String userID) {
        Object newuser = null;
        String path = "";

        if(rb_User.isChecked()){
            newuser = googleRegistration ?  new SimpleUser(name, phone, email,true) : new SimpleUser(name, email, phone, password);
            path = "Users";
        }

        if(rb_Manager.isChecked()){
            newuser = googleRegistration ?   new HotelManager(name, phone, email,true) :  new HotelManager(name, email, phone, password);
            path = "Hotel Manager";
        }

        FirebaseDatabase.getInstance().getReference(path)
        .child(userID)
        .setValue(newuser).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(getContext(),"User has ben registered successfully!", Toast.LENGTH_LONG).show();
                progressBar_Register.setVisibility(View.GONE);

                FirebaseAuth.getInstance().signOut();

                final NavController navController = Navigation.findNavController(mainRoot);
                navController.navigate(R.id.action_registration_to_login);
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

        progressBar_passwordstrength_registration.setProgressTintList(ColorStateList.valueOf(passwordStrength.getColor()));
        progressBar_passwordstrength_registration.setProgress(passwordStrength.getStrength());
        tv_passwordstrength_registration.setText(passwordStrength.getMsg());

    }
}