package com.example.ferias;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        int id = navController.getCurrentDestination().getId();
        switch (id) {
            case R.id.home_simple_user:
                navController.navigate(R.id.action_home_simple_user_self);
            break;

            case R.id.home_hotel_manager:
                navController.navigate(R.id.action_hotel_manager_home_self);
            break;
        }


        return super.onSupportNavigateUp();
    }
    /*
    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("FirstTime", false);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                boolean googleLogin = sharedPref.getBoolean("GoogleLogin", false);
    */
}