package com.example.ferias.ui;

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

public class FirstPage extends Fragment {
    private Button bt_login;
    private Button bt_loginAdmin;
    private Button bt_Register;

    public View main_root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.first_page, container, false);
        main_root=root;

        bt_login=root.findViewById(R.id.action_to_navigation_loginPage);
        bt_loginAdmin=root.findViewById(R.id.action_to_navigation_loginAdminPage);
        bt_Register=root.findViewById(R.id.action_to_navigation_RegistrationPage);
        clickListener(root);
        return root;
    }

    private void clickListener(View root) {

    }
}
