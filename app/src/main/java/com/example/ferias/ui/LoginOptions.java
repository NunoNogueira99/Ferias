package com.example.ferias.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ferias.R;

public class LoginOptions extends Fragment {

    private Button bt_LoginAsUser;
    private Button bt_LoginAsManager;
    private Button bt_RegistrationAsNormalUser;
    private Button bt_RegistrationAsHotelManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login_options, container, false);

        initializeElements(root);

        clickListener(root);
        return root;
    }

    private void initializeElements(View root) {

        bt_LoginAsUser = root.findViewById(R.id.bt_LoginAsUser);
        bt_LoginAsManager = root.findViewById(R.id.bt_LoginAsManager);
        bt_RegistrationAsNormalUser = root.findViewById(R.id.bt_RegistrationAsNormalUser);
        bt_RegistrationAsHotelManager = root.findViewById(R.id.bt_RegistrationAsHotelManager);

    }

    private void clickListener(View root) {
        bt_LoginAsUser.setOnClickListener(v -> {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("Login_Manager", false);
            editor.commit();

            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_loginOptions_to_login);
        });

        bt_LoginAsManager.setOnClickListener(v -> {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("Login_Manager", true);
            editor.commit();

            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_loginOptions_to_login);
        });
        bt_RegistrationAsNormalUser.setOnClickListener(v -> {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("Registration_Manager", false);
            editor.commit();

            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_loginOptions_to_registrationOptions);
        });

        bt_RegistrationAsHotelManager.setOnClickListener(v -> {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("Registration_Manager", true);
            editor.commit();

            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.action_loginOptions_to_registrationOptions);
        });
    }
}
