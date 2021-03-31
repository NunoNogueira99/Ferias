package com.example.ferias.ui.hotel_manager.manage_hotels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ferias.R;

public class ManageHotels extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.hotel_manager_fragment_manage_hotels, container, false);

        // Img Gallery
        LinearLayout adminGalleryItem = root.findViewById(R.id.adminGallery);

        for(int i=0; i < 6; i++)
        {
            View view = inflater.inflate(R.layout.hotel_manager_fragment_profile_gallery_item, adminGalleryItem,false);
            TextView textView = view.findViewById(R.id.imgTextadmin);

            String hotelName ="",totalProfitsByhotel=""; //sub this with actual correct values
            textView.setText(hotelName + ":n" + totalProfitsByhotel);

            ImageView imageView = view.findViewById(R.id.imgGalleryadmin);
            imageView.setImageResource(R.mipmap.ic_launcher);

            adminGalleryItem.addView(view);
        }

        return root;
    }

}
