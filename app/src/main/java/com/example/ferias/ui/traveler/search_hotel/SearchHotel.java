package com.example.ferias.ui.traveler.search_hotel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ferias.R;
import com.example.ferias.data.hotel_manager.Hotel;
import com.example.ferias.ui.traveler.favorites.RecyclerItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchHotel extends Fragment {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private TextView mResultInfo;

    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Hotel");
    private FirebaseRecyclerOptions <Hotel> options;
    private FirebaseRecyclerAdapter <Hotel,MyViewHolderClass> adapter;

    private FloatingActionButton filterBtn;
    private EditText minPriceBtn, maxPriceBtn;
    private boolean fabsOn = false;

    TextView partyMoodBtn;
    TextView chillMoodBtn;
    TextView adventureMoodBtn;
    TextView sportsMoodBtn;

    Map<Hotel,String> searchResults = new HashMap<>();
    Map<Hotel,String> filteredResults = new HashMap<>();

    //filter vars
    Float minPrice =null, maxPrice=null;
    boolean party=false,chill=false,adventure=false, sports=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.traveler_fragment_hotel_search, container, false);

        initializeElements(root);
        clickListener(root);

        setQuery(root);
        //loadData(getArguments().getString("inputText"),root);
        return root;
    }

    private void setQuery(View root) {
        String modsWanted = getArguments().getString("modsWanted");

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
            Query query = databaseReference.orderByChild("address/city").startAt(mSearchField.getText().toString().toLowerCase()).endAt(mSearchField.getText().toString().toLowerCase() + "\uf8ff");

            if(mSearchField.getText().toString().isEmpty())
                mResultInfo.setText("All");
            else
                mResultInfo.setText(mSearchField.getText().toString());
            loadData(query,root);
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fabsOn)
                {
                    minPriceBtn.setVisibility(View.VISIBLE);
                    maxPriceBtn.setVisibility(View.VISIBLE);
                    partyMoodBtn.setVisibility(View.VISIBLE);
                    chillMoodBtn.setVisibility(View.VISIBLE);
                    adventureMoodBtn.setVisibility(View.VISIBLE);
                    sportsMoodBtn.setVisibility(View.VISIBLE);
                    fabsOn=true;
                }
                else
                {
                    minPriceBtn.setVisibility(View.GONE);
                    maxPriceBtn.setVisibility(View.GONE);
                    partyMoodBtn.setVisibility(View.GONE);
                    chillMoodBtn.setVisibility(View.GONE);
                    adventureMoodBtn.setVisibility(View.GONE);
                    sportsMoodBtn.setVisibility(View.GONE);
                    fabsOn=false;
                }
            }
        });

        mResultList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mResultList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //go-to hotel page
                Bundle bundle = new Bundle();
                List<String>keys=new ArrayList<>();

                if(filteredResults.isEmpty()) {
                    for(Map.Entry<Hotel,String> entry : searchResults.entrySet())
                        keys.add(entry.getValue());
                }
                else
                {
                    for(Map.Entry<Hotel,String> entry : filteredResults.entrySet())
                        keys.add(entry.getValue());
                }

                bundle.putString("clickDetails", keys.get(position));
                Navigation.findNavController(root).navigate(R.id.action_traveler_search_to_traveler_hotelview, bundle);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

        maxPriceBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!maxPriceBtn.getText().toString().isEmpty())
                    maxPrice= Float.parseFloat(maxPriceBtn.getText().toString());
                else
                    maxPrice=null;
                applyFilters(minPrice,maxPrice,party,chill,adventure,sports);

            }
        });
        minPriceBtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                    if(!minPriceBtn.getText().toString().isEmpty())
                        minPrice= Float.parseFloat(minPriceBtn.getText().toString());
                    else
                        minPrice=null;
                    applyFilters(minPrice,maxPrice,party,chill,adventure,sports);

            }
        });

        partyMoodBtn.setOnClickListener(v -> {
            if(!party)
            {
                partyMoodBtn.setBackgroundResource(R.drawable.rounded_rectangle_active);
                party=true;
            }
            else
            {
                partyMoodBtn.setBackgroundResource(R.drawable.shape_search_1_active);
                party=false;
            }
            applyFilters(minPrice,maxPrice,party,chill,adventure,sports);
        });
        chillMoodBtn.setOnClickListener(v -> {
            if(!chill)
            {
                chillMoodBtn.setBackgroundResource(R.drawable.rounded_rectangle_active);
                chill=true;
            }
            else
            {
                chillMoodBtn.setBackgroundResource(R.drawable.shape_search_1_active);
                chill=false;
            }
            applyFilters(minPrice,maxPrice,party,chill,adventure,sports);
        });
        adventureMoodBtn.setOnClickListener(v -> {
            if(!adventure)
            {
                adventureMoodBtn.setBackgroundResource(R.drawable.rounded_rectangle_active);
                adventure=true;
            }
            else
            {
                adventureMoodBtn.setBackgroundResource(R.drawable.shape_search_1_active);
                adventure=false;
            }
            applyFilters(minPrice,maxPrice,party,chill,adventure,sports);
        });
        sportsMoodBtn.setOnClickListener(v -> {
            if(!sports)
            {
                sportsMoodBtn.setBackgroundResource(R.drawable.rounded_rectangle_active);
                sports=true;
            }
            else
            {
                sportsMoodBtn.setBackgroundResource(R.drawable.shape_search_1_active);
                sports=false;
            }
            applyFilters(minPrice,maxPrice,party,chill,adventure,sports);
        });
    }

    private void loadData(Query query,View root) {
        searchResults = new HashMap<>();
        filteredResults = new HashMap<>();

        options = new FirebaseRecyclerOptions.Builder<Hotel>().setQuery(query, Hotel.class).build();

        adapter= new FirebaseRecyclerAdapter<Hotel, MyViewHolderClass>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderClass holder, int position, @NonNull Hotel model) {
                searchResults.put(model,getRef(position).getKey());

                holder.setName(model.getName());
                holder.setCity(model.getAddress().getCity());
                holder.setPrice(Float.toString(model.getPrice()));
                Picasso.get().load(model.getCoverPhoto()).into(holder.image);
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

        if(getArguments().getString("inputText")==null)
            mResultInfo.setText("All");
        else
            mResultInfo.setText(getArguments().getString("inputText"));

        filterBtn= root.findViewById(R.id.filter_btn);
        minPriceBtn = root.findViewById(R.id.min_price);
        maxPriceBtn = root.findViewById(R.id.max_price);
        partyMoodBtn = root.findViewById(R.id.searchFilter_party);;
        chillMoodBtn = root.findViewById(R.id.searchFilter_Chill);;
        adventureMoodBtn = root.findViewById(R.id.searchFilter_Adventure);;
        sportsMoodBtn = root.findViewById(R.id.searchFilter_sports);;

        minPriceBtn.setVisibility(View.GONE);
        maxPriceBtn.setVisibility(View.GONE);
        partyMoodBtn.setVisibility(View.GONE);
        chillMoodBtn.setVisibility(View.GONE);
        adventureMoodBtn.setVisibility(View.GONE);
        sportsMoodBtn.setVisibility(View.GONE);
    }

    private void applyFilters(Float minPrice, Float maxPrice, boolean party, boolean chill, boolean adventure, boolean sports)
    {


        //filteredResults = new HashMap<>(searchResults);
        filteredResults = new HashMap<>();
        for(Map.Entry<Hotel,String> entry : searchResults.entrySet())
            filteredResults.put(entry.getKey(),entry.getValue());
        if(minPrice != null){
            for (Map.Entry<Hotel,String> entry : searchResults.entrySet()) {
                if(entry.getKey().getPrice()<minPrice)
                {
                    filteredResults.remove(entry.getKey());
                }
            }
        }

        if(maxPrice != null){
            for (Map.Entry<Hotel,String> entry : searchResults.entrySet()) {
                if(entry.getKey().getPrice()> maxPrice)
                {
                    filteredResults.remove(entry.getKey());
                }
            }
        }
        if(party)
        {
            for (Map.Entry<Hotel,String> entry : searchResults.entrySet()) {
                if(entry.getKey().getMoods().getMoods().get("Nightlife, clubs and parties")==false)
                {
                    filteredResults.remove(entry.getKey());
                }
            }
        }
        if(chill)
        {
            for (Map.Entry<Hotel,String> entry : searchResults.entrySet()) {
                if(entry.getKey().getMoods().getMoods().get("Very chill, perfect to relax")==false)
                {
                    filteredResults.remove(entry.getKey());
                }
            }
        }
        if(adventure)
        {
            for (Map.Entry<Hotel,String> entry : searchResults.entrySet()) {
                if(entry.getKey().getMoods().getMoods().get("Ready to be explored! Embark on an adventure")==false)
                {
                    filteredResults.remove(entry.getKey());
                }
            }
        }
        if(sports)
        {
            for (Map.Entry<Hotel,String> entry : searchResults.entrySet()) {
                if(entry.getKey().getMoods().getMoods().get("You can do some sport activities")==false)
                {
                    filteredResults.remove(entry.getKey());
                }
            }
        }

        adapterFilteredResults adapterFilteredResults=new adapterFilteredResults(filteredResults.keySet());
        mResultList.setAdapter(adapterFilteredResults);
    }
}