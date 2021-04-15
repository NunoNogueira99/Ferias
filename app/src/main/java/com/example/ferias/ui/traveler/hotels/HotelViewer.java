package com.example.ferias.ui.traveler.hotels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ferias.R;
import com.example.ferias.data.hotel_manager.Hotel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelViewer extends Fragment {
    private ImageButton favBtn;
    private TextView hotelName, hotelPrice, hotelInfo, numOfRatings;
    private RatingBar rating;
    private String hotelKey;
    private DatabaseReference databaseReference;
    private ImageView iv_hotel_cover_photo;
    private ImageSlider othersphotosslider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.traveler_fragment_hotel_view, container, false);

        initializeElements(root);
        clickListener(root);

        return root;
    }

    private void initializeElements(View root) {
        hotelName = root.findViewById(R.id.traveler_hotelview_hotelName);
        hotelPrice = root.findViewById(R.id.traveler_hotelview_hotelPrice);
        hotelInfo =root.findViewById(R.id.traveler_hotelview_hotelinfo);
        favBtn = root.findViewById(R.id.add_to_favorites_hotelfound);
        rating = root.findViewById(R.id.ratingBar);
        numOfRatings = root.findViewById(R.id.number_of_review);

        iv_hotel_cover_photo = root.findViewById(R.id.iv_hotel_cover_photo);
        othersphotosslider = root.findViewById(R.id.hotel_slider_photos);

        hotelKey = getArguments().getString("clickDetails");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Hotel");
    }

    private void clickListener(View root) {
        databaseReference.child(hotelKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Hotel hotel = snapshot.getValue(Hotel.class);
                if(hotel != null)
                {
                    rating.setRating(hotel.getStars());
                    hotelName.setText(hotel.getName());
                    hotelPrice.setText(String.valueOf(hotel.getPrice()));
                    hotelInfo.setText(hotel.getDescription());
                    numOfRatings.setText(String.valueOf(hotel.getRate()));

                    Glide.with(getView())
                    .load(hotel.getCoverPhoto())
                    .placeholder(R.drawable.admin_backgrounf_pic)
                    .fitCenter()
                    .into(iv_hotel_cover_photo);

                    List<SlideModel> slideModelList = new ArrayList<>();

                    for(String photo : hotel.getOtherPhotos()){
                        slideModelList.add(new SlideModel(String.valueOf(photo),"", ScaleTypes.FIT));
                    }
                    othersphotosslider.setImageList(slideModelList, ScaleTypes.FIT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        favBtn.setOnClickListener(v ->
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Map <String, String> map = new HashMap<>();

            DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference().child("Traveler/" + user.getUid());
            dbUsers.child("Favs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        ArrayList<String> list = (ArrayList<String>)snapshot.getValue();
                        add_removeValue(list, map, dbUsers);
                    }
                    else{
                        map.put("0", hotelKey);
                        dbUsers.child("Favs").setValue(map);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            favBtn.setImageResource(R.drawable.ic_favorite_full);
        });

    }

    private void add_removeValue(ArrayList<String> list, Map <String, String> map,DatabaseReference dbUsers) {
        map = new HashMap<>();
        if(!list.contains(hotelKey)){
            for (Integer i=0; i<list.size(); i++)
            {
                map.put(i.toString(), list.get(i));
            }
            Integer sizeValue = list.size();
            map.put(sizeValue.toString(),hotelKey);
        } else
        {
            list.remove(hotelKey);
            for (Integer i=0; i<list.size(); i++)
            {
                map.put(i.toString(), list.get(i));
            }
            favBtn.setImageResource(R.drawable.ic_favorites);
        }
        dbUsers.child("Favs").setValue(map);
    }
}