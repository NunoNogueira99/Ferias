package com.example.ferias.ui.hotel_manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ferias.R;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        // Img Gallery
        LinearLayout adminGalleryItem = findViewById(R.id.adminGallery);
        LayoutInflater inflater = LayoutInflater.from(this);

        for(int i=0; i < 6; i++)
        {
            View view = inflater.inflate(R.layout.admin_gallery_item, adminGalleryItem,false);
            TextView textView = view.findViewById(R.id.imgTextadmin);

            String hotelName ="",totalProfitsByhotel=""; //sub this with actual correct values
            textView.setText(hotelName + ":\n" + totalProfitsByhotel);

            ImageView imageView = view.findViewById(R.id.imgGalleryadmin);
            imageView.setImageResource(R.mipmap.ic_launcher);

            adminGalleryItem.addView(view);
        }

    }
}
