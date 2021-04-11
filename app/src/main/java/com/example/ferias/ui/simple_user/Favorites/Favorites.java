package com.example.ferias.ui.simple_user.Favorites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ferias.R;
import com.example.ferias.data.hotel_manager.Hotel;
import com.example.ferias.data.simple_user.SimpleUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Favorites extends Fragment {

    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Hotel");
    private RecyclerView mFavList;
    private ArrayList <Hotel> list = new ArrayList<>();
    TextView name, city, price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.simple_user_fragment_favorites_hotels, container, false);


        initializeElements(root);
        //clickListener(root);

        getFavsList(root);
        return root;
    }

    private void getFavsList(View root) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference().child("Users/" + user.getUid());

        dbUsers.child("Favs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    ArrayList<String> keys = (ArrayList<String>)snapshot.getValue();
                    populateRecyclerView(root,keys);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeElements(View root) {
        name= root.findViewById(R.id.Favs_listName);
        city= root.findViewById(R.id.Favs_listCity);
        price = root.findViewById(R.id.Favs_listPrice);


    }

    private void populateRecyclerView(View root, ArrayList<String> keys) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(keys.contains(ds.getKey())){
                        Hotel hotel = ds.getValue(Hotel.class);

                        list.add(hotel);
                    }
                }
                // Lookup the recyclerview in activity layout
                RecyclerView rvHotels = root.findViewById(R.id.FavList_recyclerView);

                // Create adapter passing in the sample user data
                MyViewHolderClassFavs adapter = new MyViewHolderClassFavs(list);
                // Attach the adapter to the recyclerview to populate items
                rvHotels.setAdapter(adapter);
                // Set layout manager to position the items
                rvHotels.setLayoutManager(new GridLayoutManager(getContext(),2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}