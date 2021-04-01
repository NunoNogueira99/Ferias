package com.example.ferias.ui.simple_user.search_hotel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ferias.R;
import com.example.ferias.data.hotel_manager.Hotel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchHotel extends Fragment {

    private EditText mSearchField;
    private MaterialButton mSearchBtn;
    private RecyclerView mResultList;
    private TextView mResultInfo;

    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Hotel");
    private FirebaseRecyclerOptions <Hotel> options;
    private FirebaseRecyclerAdapter <Hotel,MyViewHolderClass> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.simple_user_fragment_hotel_search, container, false);

        initializeElements(root);
        clickListener(root);

        loadData(getArguments().getString("inputText"));
        return root;
    }

    private void clickListener(View root) {
        mSearchBtn.setOnClickListener(v -> {
            mResultInfo.setText(mSearchField.getText().toString());
            loadData(mSearchField.getText().toString());
        });
    }

    private void loadData(String searchText) {
        Query query= databaseReference.orderByChild("_Address/city").startAt(searchText).endAt(searchText + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Hotel>().setQuery(query, Hotel.class).build();

        adapter= new FirebaseRecyclerAdapter<Hotel, MyViewHolderClass>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderClass holder, int position, @NonNull Hotel model) {
                holder.name.setText(model.get_Name());
                holder.city.setText(model.get_Address().getCity());
                //holder.price.setText(model.get_Price());
                //Picasso.get().load(model.getImageURL()).into(holder.image);
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