package com.example.ferias;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*String language;
        if (user != null) {
            language = user.getLanguage().toUpperCase();
        } else {
            language = "EN";
        }

        String primaryLocale = getResources().getConfiguration().getLocales().get(0).toString().toUpperCase();
        if (!primaryLocale.contains(language)) {
            switch (language) {
                case "EN":
                case "EN_US":
                    setLocale("en");
                    break;

                case "PT":
                case "PT_PT":
                    setLocale("pt");
                    break;

                case "IT":
                case "IT_IT":
                    setLocale("it");
                    break;
            }
        }*/

    }



    /*private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = getActivity().getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }*/
}