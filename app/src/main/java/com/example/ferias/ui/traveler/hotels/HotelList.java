package com.example.ferias.ui.traveler.hotels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ferias.R;

public class HotelList extends Fragment {

    ImageButton gobackhome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.traveler_fragment_hotel_list, container, false);

        gobackhome = root.findViewById(R.id.gobacktohome_from_hotellist);

        clickListener(root);

        return root;
    }

    private void clickListener(View root) {

        gobackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                //navController.navigate(R.id.action_traveler_hotellist_to_traveler_home);
            }
        });

    }
}
