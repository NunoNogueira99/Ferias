package com.example.ferias.ui.simple_user.hotels;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferias.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.InternalHelpers;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HotelViewer extends Fragment {
    ImageButton favBtn;
    TextView hotelName, hotelPrice, hotelInfo, numOfRatings;
    RatingBar rating;
    String hotelKey;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.simple_user_fragment_hotel_view, container, false);

        initializeElements(root);
        clickListener(root);

        return root;
    }

    private void initializeElements(View root) {
        hotelName = root.findViewById(R.id.simple_user_hotelview_hotelName);
        hotelPrice = root.findViewById(R.id.simple_user_hotelview_hotelPrice);
        hotelInfo =root.findViewById(R.id.simple_user_hotelview_hotelinfo);
        favBtn = root.findViewById(R.id.add_to_favorites_hotelfound);
        rating = root.findViewById(R.id.ratingBar);
        numOfRatings = root.findViewById(R.id.number_of_review);

        hotelKey = getArguments().getString("clickDetails");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Hotel");
    }

    private void clickListener(View root) {
        databaseReference.child(hotelKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String f1 = snapshot.child("_Stars").getValue().toString();
                    rating.setRating(Float.parseFloat(f1));
                    //Picasso.get().load(ImageURL).into(imageView);
                    hotelName.setText(snapshot.child("_Name").getValue().toString());
                    hotelPrice.setText(snapshot.child("_Price").getValue().toString());
                    hotelInfo.setText(snapshot.child("_Description").getValue().toString());
                    numOfRatings.setText(snapshot.child("_Rate").getValue().toString());
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

            DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference().child("Users/" + user.getUid());
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
