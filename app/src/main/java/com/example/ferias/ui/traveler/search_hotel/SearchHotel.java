package com.example.ferias.ui.traveler.search_hotel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ferias.R;
import com.example.ferias.data.hotel_manager.Hotel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.squareup.picasso.Picasso;

public class SearchHotel extends Fragment {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private TextView mResultInfo;

    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Hotel");
    private FirebaseRecyclerOptions <Hotel> options;
    private FirebaseRecyclerAdapter <Hotel,MyViewHolderClass> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.traveler_fragment_hotel_search, container, false);

        initializeElements(root);
        clickListener(root);

        initialSearch(root);
        //loadData(getArguments().getString("inputText"),root);
        return root;
    }

    private void initialSearch(View root) {
        String modsWanted = getArguments().getString("modsWanted");
        String inputSearch = getArguments().getString("inputText");

        Query query;
        if(modsWanted != null)
        {
            switch(modsWanted)
            {
                case "Party":
                    query= databaseReference.orderByChild("moods/moods/Nightlife, clubs and parties").equalTo(true);
                    loadData(query,root);
                    return;
                case "Chill":
                    query= databaseReference.orderByChild("moods/moods/Very chill, perfect to relax").equalTo(true);
                    loadData(query,root);
                    return;
                case "Adventure":
                    query= databaseReference.orderByChild("moods/moods/Ready to be explored! Embark on an adventure").equalTo(true);
                    loadData(query,root);
                    return;
                case "Sports":
                    query= databaseReference.orderByChild("moods/moods/You can do some sport activities").equalTo(true);
                    loadData(query,root);
                    return;
            }
        }
        else
        {
            query= databaseReference.orderByChild("address/city").startAt(mSearchField.getText().toString().toUpperCase()).endAt(mSearchField.getText().toString().toLowerCase() + "\uf8ff");
            loadData(query,root);
        }
    }

    private void clickListener(View root) {
        mSearchBtn.setOnClickListener(v -> {
            Query query = databaseReference.orderByChild("address/city").startAt(mSearchField.getText().toString().toUpperCase()).endAt(mSearchField.getText().toString().toLowerCase() + "\uf8ff");
            mResultInfo.setText(mSearchField.getText().toString());
            loadData(query,root);
        });
    }

    private void loadData(Query query,View root) {
        options = new FirebaseRecyclerOptions.Builder<Hotel>().setQuery(query, Hotel.class).build();

        adapter= new FirebaseRecyclerAdapter<Hotel, MyViewHolderClass>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderClass holder, int position, @NonNull Hotel model) {
                holder.setName(model.getName());
                holder.setCity(model.getAddress().getCity());
                holder.setPrice(Float.toString(model.getPrice()));
                Picasso.get().load(model.getCoverPhoto()).into(holder.image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("clickDetails", getRef(position).getKey());
                        Navigation.findNavController(root).navigate(R.id.action_traveler_search_to_traveler_hotelview, bundle);

                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_layout, parent,false);

                return new MyViewHolderClass(v);
            }
        };
        adapter.startListening();
        mResultList.setAdapter(adapter);
    }

    private void initializeElements(View root) {
        mSearchField = root.findViewById(R.id.search_input_location);
        mSearchBtn = root.findViewById(R.id.search_btn);
        mResultList = root.findViewById(R.id.searchResults);
        mResultList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mResultInfo = root.findViewById(R.id.searchResultsInfoText);
        mResultInfo.setText(getArguments().getString("inputText"));

    }
}