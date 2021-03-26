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

public class RegistrationOptions extends Fragment {
    private Button bt_registration;
    private Button bt_linkAccount;
    private Button bt_registerAsAdmin;

    public View main_root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.registration_options, container, false);
        main_root=root;

        bt_registration=root.findViewById(R.id.bt_action_to_navigation_RegistrationPage);
        bt_linkAccount=root.findViewById(R.id.bt_action_to_navigation_linkGoogle);
        bt_registerAsAdmin=root.findViewById(R.id.bt_action_to_navigation_RegistrationAsAdmin);
        clickListener(root);
        return root;
    }

    private void clickListener(View root) {
    }
}
