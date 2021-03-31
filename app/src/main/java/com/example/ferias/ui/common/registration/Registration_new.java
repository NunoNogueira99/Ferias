/*
This is the version created by me (Martin), that is going to replace Registration.java
*/

package com.example.ferias.ui.common.registration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ferias.R;
import com.example.ferias.data.PasswordStrength;
import com.example.ferias.data.hotel_manager.HotelManager;
import com.example.ferias.data.simple_user.User;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;


public class Registration_new extends Fragment {

    private FirebaseAuth firebaseAuth;

    private Button bt_RegisterUser;
    private ImageButton bt_Backhome_Registration;
    private ProgressBar progressBar_Register;

    private TextInputEditText et_Name, et_Surname;

    private MaterialCardView cw_chooseHotelmanager, cw_chooseTraveler;
    Boolean is_account_type_chosen = false;
    String account_type_chosen = "Traveler"; //Default, it can be Traveler or Hotelmanager

    private CountryCodePicker ccp_PhoneCode;
    private TextInputEditText et_Phone;

    private TextInputLayout tilEmail_Registration;
    private TextInputEditText et_EmailAddress;

    private LinearLayout ll_Password;
    private TextInputEditText et_Password;
    private TextView tv_passwordstrength_registration;
    private ProgressBar progressBar_passwordstrength_registration;

    private boolean googleRegistration;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        initializeElements(root);

        clickListeners(root);

        return root;
    }

    private void initializeElements(View root) {
        bt_Backhome_Registration =  root.findViewById(R.id.bt_Backhome_Registration);

        cw_chooseHotelmanager = root.findViewById(R.id.choose_hotelmanager);
        cw_chooseTraveler = root.findViewById(R.id.choose_traveler);
        cw_chooseTraveler.setChecked(true); //default

        et_Name = root.findViewById(R.id.et_Name_Registration);
        et_Surname = root.findViewById(R.id.et_Surname_Registration);

        ccp_PhoneCode = root.findViewById(R.id.ccp_PhoneCode_Registration);
        et_Phone =  root.findViewById(R.id.et_Phone_Registration);
        ccp_PhoneCode.registerCarrierNumberEditText(et_Phone);

        tilEmail_Registration = root.findViewById(R.id.tilEmail_Registration);
        et_EmailAddress = root.findViewById(R.id.et_Email_Registration);

        ll_Password  = root.findViewById(R.id.ll_Password);
        et_Password = root.findViewById(R.id.et_Password_Registration);

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

        cw_chooseHotelmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!cw_chooseHotelmanager.isChecked()) || (cw_chooseTraveler.isChecked())){
                    cw_chooseHotelmanager.setChecked(true);
                    is_account_type_chosen = true;
                    account_type_chosen = "Hotelmanager";
                    if(cw_chooseTraveler.isChecked())
                        cw_chooseTraveler.setChecked(false);
                }
                else{
                    cw_chooseHotelmanager.setChecked(false);
                    is_account_type_chosen = false;
                }
            }
        });

        cw_chooseTraveler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!cw_chooseTraveler.isChecked()) || (cw_chooseHotelmanager.isChecked())){
                    cw_chooseTraveler.setChecked(true);
                    is_account_type_chosen = true;
                    account_type_chosen = "Traveler";
                    if(cw_chooseHotelmanager.isChecked())
                        cw_chooseHotelmanager.setChecked(false);
                }
                else{
                    cw_chooseTraveler.setChecked(false);
                    is_account_type_chosen = false;
                }
            }
        });
    }

    private void verifyData(){

        String name = et_Name.getText().toString().trim();
        String surname = et_Surname.getText().toString().trim();
        String phone = ccp_PhoneCode.getFormattedFullNumber();
        String email = et_EmailAddress.getText().toString().trim();
        String password = et_Password.getText().toString().trim();
        boolean error = false;

        if(name.isEmpty()){
            et_Name.setError("Name is required");
            et_Name.requestFocus();
            error = true;
        }

        if(surname.isEmpty()){
            et_Surname.setError("Name is required");
            et_Surname.requestFocus();
            error = true;
        }

        if(!ccp_PhoneCode.isValidFullNumber()){
            et_Phone.setError("Phone is required or is not valid");
            et_Phone.requestFocus();
            error = true;
        }

        if(!googleRegistration){
            if(email.isEmpty()){
                et_EmailAddress.setError("Email address is required");
                et_EmailAddress.requestFocus();
                error = true;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                et_EmailAddress.setError("Please provide a valid email address");
                et_EmailAddress.requestFocus();
                error = true;
            }

            PasswordStrength passwordStrength = PasswordStrength.calculate(password);
            if(password.isEmpty() || passwordStrength.strength <= 1){
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Password strength error");
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

        registerUser(name,surname,phone,email,password);
    }

    private void registerUser(String name, String surname, String phone, String email, String password) {

        progressBar_Register.setVisibility(View.VISIBLE);

        if(!googleRegistration){
            firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"An email has been sent to activate your account!", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                }
                else {
                    Toast.makeText(getContext(),"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    progressBar_Register.setVisibility(View.GONE);
                    return;
                }
            });
        }

        registerInFirebase(name,surname,phone,email,password);

    }

    private void registerInFirebase(String name, String surname, String phone, String email, String password) {
        Object newuser = null;
        String path = "";

        if(account_type_chosen == "Traveler"){
            //TO-DO: register the user with name and surname, need to define the structure from firebase
            newuser = googleRegistration ?  new User(name, phone, email,true) : new User(name, email, phone, password);
            path = "Users";
        }

        if(account_type_chosen == "Hotelmanager"){
            //TO-DO: register the user with name and surname, no full company name, need to define the structure from firebase
            newuser = googleRegistration ?   new HotelManager(name, phone, email,true) :  new HotelManager(name, email, phone, password);;
            path = "Hotel Manager";
        }

        FirebaseDatabase.getInstance().getReference(path)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(newuser).addOnCompleteListener(task1 -> {
            if(task1.isSuccessful()){
                Toast.makeText(getContext(),"User has ben registered successfully!", Toast.LENGTH_LONG).show();
                progressBar_Register.setVisibility(View.GONE);

                final NavController navController = Navigation.findNavController(getView());
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

        progressBar_passwordstrength_registration.setProgressTintList(ColorStateList.valueOf(passwordStrength.color));
        progressBar_passwordstrength_registration.setProgress(passwordStrength.strength);
        tv_passwordstrength_registration.setText(passwordStrength.msg);

    }
}