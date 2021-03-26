package com.example.ferias.ui.profile;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ferias.MainActivity;
import com.example.ferias.R;
import com.example.ferias.data.InternalStorage;
import com.example.ferias.data.simple_user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Locale;

public class Preferences extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private User user;

    private RadioGroup rg_distance, rg_currency, rg_language;
    private RadioButton rb_Km, rb_Mi;
    private RadioButton rb_Euro, rb_Dolar, rb_Ienes;
    private RadioButton rb_English, rb_Portuguese, rb_Italian;

    private Button bt_save_preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile_preferences, container, false);

        readUserData();

        initializeElements(root);

        clickListeners(root);

        return root;
    }

    private void initializeElements(View root) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        rg_distance = root.findViewById(R.id.rg_distance);
        rg_currency = root.findViewById(R.id.rg_currency);
        rg_language = root.findViewById(R.id.rg_language);

        rb_Km = root.findViewById(R.id.rb_Km);
        rb_Mi = root.findViewById(R.id.rb_Mi);

        rb_Euro = root.findViewById(R.id.rb_Euro);
        rb_Dolar = root.findViewById(R.id.rb_Dolar);
        rb_Ienes = root.findViewById(R.id.rb_Ienes);

        rb_English = root.findViewById(R.id.rb_English);
        rb_Portuguese = root.findViewById(R.id.rb_Portuguese);
        rb_Italian = root.findViewById(R.id.rb_Italian);

        bt_save_preferences = root.findViewById(R.id.bt_save_preferences);

    }

    private void clickListeners(View root) {
        bt_save_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });
    }

    private void readUserData() {
        try {
            user = (User) InternalStorage.readObject(getContext(), "User");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        loadDatatoElements();
    }

    private void loadDatatoElements() {
        if (user != null) {
            String units_distance = user.getUnitsDistance();
            String units_currency = user.getUnitsCurrency();
            String language = user.getLanguage();

            if (!units_distance.isEmpty() && !units_currency.isEmpty() && !language.isEmpty()) {
                switch (language) {
                    case "en":
                        rb_English.toggle();
                    break;

                    case "pt":
                        rb_Portuguese.toggle();
                    break;

                    case "it":
                        rb_Italian.toggle();
                    break;
                }

                switch (units_currency) {
                    case "euro":
                        rb_Euro.toggle();
                    break;
                    case "dolar":
                        rb_Dolar.toggle();
                    break;
                    case "ienes":
                        rb_Ienes.toggle();
                    break;
                }

                switch (units_distance) {
                    case "km":
                        rb_Km.toggle();
                    break;

                    case "mi":
                        rb_Mi.toggle();
                    break;
                }
            }
        }
    }

    private void savePreferences() {

        String units_distance, units_currency, language_chosen;

        int select_distance = rg_distance.getCheckedRadioButtonId();
        int select_currency = rg_currency.getCheckedRadioButtonId();
        int select_language = rg_language.getCheckedRadioButtonId();

        switch (select_distance) {
            case R.id.rb_Km:
                units_distance = "km";
            break;

            case R.id.rb_Mi:
                units_distance = "mi";
            break;

            default:
                units_distance = "km";
        }

        switch (select_currency) {
            case R.id.rb_Euro:
                units_currency = "euro";
            break;

            case R.id.rb_Dolar:
                units_currency = "dolar";
            break;

            case R.id.rb_Ienes:
                units_currency = "ienes";
            break;

            default:
                units_currency = "euro";
        }

        switch (select_language) {
            case R.id.rb_English:
                language_chosen = "en";
            break;

            case R.id.rb_Portuguese:
                language_chosen = "pt";
            break;

            case R.id.rb_Italian:
                language_chosen = "it";
            break;

            default:
                language_chosen = "en";
        }

        if (!user.getLanguage().equals(language_chosen)) {
            setLocale(language_chosen);
            user.setLanguage(language_chosen);
        }

        user.setUnitsDistance(units_distance);
        user.setUnitsCurrency(units_currency);

        databaseReference.setValue(user);
    }

    private void setLocale(String language) {

        Configuration conf = new Configuration(getContext().getResources().getConfiguration());
        conf.locale = new Locale(language);
        getContext().getResources().updateConfiguration(conf, getContext().getResources().getDisplayMetrics());

        Intent refresh = new Intent(getContext(), MainActivity.class);
        startActivity(refresh);
    }
}