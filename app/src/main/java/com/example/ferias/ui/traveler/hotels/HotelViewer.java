package com.example.ferias.ui.traveler.hotels;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ferias.R;
import com.example.ferias.data.GenerateUniqueIds;
import com.example.ferias.data.hotel_manager.Hotel;
import com.example.ferias.data.traveler.Booking;
import com.example.ferias.data.traveler.Traveler;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelViewer extends Fragment {
    private ImageButton favBtn, backBtn;
    private TextView hotelName, hotelPrice, hotelInfo, numOfRatings;
    private RatingBar rating;
    private String hotelKey;
    private DatabaseReference databaseReference, databaseReferenceHotel, databaseReferenceTraveler;
    private ImageView iv_hotel_cover_photo;
    private ImageSlider othersphotosslider;

    private RecyclerView rvFeatures, recyclerViewSimilar;
    private final ArrayList<Hotel> similarHotelKeys = new ArrayList<>();
    private Hotel hotel;
    String userID;

    ////////////////////////////////

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    private Booking booking;


    private Button ivBooking;
    private MaterialTextView tv_date_start, tv_date_end;
    private EditText tv_adults, tv_children;
    private String hotelID;

    private Date start_date, end_date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.traveler_fragment_hotel_view, container, false);

        initializeElements(root);
        clickListener(root);

        getCurrentHotelInfo(root);
        //setHotelFeatures(root);

        return root;
    }

    private void initializeElements(View root) {
        hotelName = root.findViewById(R.id.traveler_hotelview_hotelName);
        hotelPrice = root.findViewById(R.id.traveler_hotelview_hotelPrice);
        hotelInfo = root.findViewById(R.id.traveler_hotelview_hotelinfo);
        favBtn = root.findViewById(R.id.add_to_favorites_hotelfound);
        rating = root.findViewById(R.id.ratingBar);
        numOfRatings = root.findViewById(R.id.number_of_review);

        iv_hotel_cover_photo = root.findViewById(R.id.iv_hotel_cover_photo);
        othersphotosslider = root.findViewById(R.id.hotel_slider_photos);

        hotelKey = getArguments().getString("clickDetails");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Hotel");
        recyclerViewSimilar = root.findViewById(R.id.similarhotels_Rv);

        rvFeatures = root.findViewById(R.id.features_gridview);

        backBtn= root.findViewById(R.id.bt_traveler_hotel_view_back);
        backBtn.bringToFront();
        ////////////////////////////////


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        hotelID = getArguments().getString("clickDetails");
        databaseReference = FirebaseDatabase.getInstance().getReference("Hotel");
        ivBooking = root.findViewById(R.id.iv_Booking);
        tv_date_start = root.findViewById(R.id.ti_choosen_start_date);
        tv_date_end = root.findViewById(R.id.ti_choosen_end_date);
        tv_adults = root.findViewById(R.id.booking_btn_choosen_adults);
        tv_children = root.findViewById(R.id.booking_btn_choosen_children);
    }

    private void clickListener(View root) {
        databaseReference.child(hotelKey).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    Glide.with(getActivity())
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


        backBtn.setOnClickListener(v -> Navigation.findNavController(getView()).navigate(R.id.action_traveler_hotelview_to_traveler_home));
        /////////////////////////////////////

        final String[] string_date = new String[1];

        tv_date_start.setOnClickListener(v -> {
            final DatePickerDialog[] picker = new DatePickerDialog[1];
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker[0] = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar date = Calendar.getInstance();
                            date.set(year, monthOfYear, dayOfMonth);
                            start_date = date.getTime();
                            string_date[0] = dayOfMonth + "/" + monthOfYear + "/" + year;
                            tv_date_start.setText(string_date[0]);
                        }
                    }, year, month, day);
            picker[0].show();

        });


        tv_date_end.setOnClickListener(v -> {
            final DatePickerDialog[] picker = new DatePickerDialog[1];
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker[0] = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar date = Calendar.getInstance();
                            date.set(year, monthOfYear, dayOfMonth);
                            end_date = date.getTime();
                            string_date[0] = dayOfMonth + "/" + monthOfYear + "/" + year;
                            tv_date_end.setText(string_date[0]);
                        }
                    }, year, month, day);

            picker[0].show();

        });


        ivBooking.setOnClickListener(v -> {
            registerOnFirebase();
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

    private boolean verifyData() {
        int adults, children;
        if(tv_adults.getText().toString().isEmpty())
            adults = 0;
        else
            adults = Integer.parseInt(tv_adults.getText().toString().trim());

        if(tv_children.getText().toString().isEmpty())
            children = 0;
        else
            children = Integer.parseInt(tv_children.getText().toString().trim());


        /////////////////
        float price_night = Float.parseFloat(hotelPrice.getText().toString());

        float price = 0;


        int n_nights = (int) (end_date.getTime() - start_date.getTime()) / (1000 * 60 * 60 * 24);

        price = price_night * n_nights;

        ////////////////

        userID = firebaseUser.getUid();

        booking = new Booking(hotelID, userID, start_date, end_date, adults, children, price);

        if (booking != null) {
            return true;
        }
        return false;
    }

    private void registerOnFirebase() {

        if (verifyData()) {
            String reservationID = GenerateUniqueIds.generateId();
            FirebaseDatabase.getInstance().getReference("Booking").child(reservationID).setValue(booking).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    Toast.makeText(getContext(), "Booking has ben registered successfully!", Toast.LENGTH_LONG).show();

                    databaseReferenceTraveler = FirebaseDatabase.getInstance().getReference().child("Traveler").child(firebaseUser.getUid());

                    databaseReferenceTraveler.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Traveler user = snapshot.getValue(Traveler.class);

                            user.addBooking(reservationID);
                            databaseReferenceTraveler.setValue(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("ERROR", "getUser:onCancelled", error.toException());
                        }
                    });

                    databaseReferenceHotel = FirebaseDatabase.getInstance().getReference().child("Hotel").child(hotelID);

                    databaseReferenceHotel.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Hotel hotel = snapshot.getValue(Hotel.class);

                            hotel.addBooking(reservationID);
                            databaseReferenceHotel.setValue(hotel);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("ERROR", "getUser:onCancelled", error.toException());
                        }
                    });

                    final NavController navController = Navigation.findNavController(getView());
                    navController.navigate(R.id.action_traveler_hotelview_to_traveler_home);
                } else {
                    Toast.makeText(getContext(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void getCurrentHotelInfo(View root) {
        //---
        //get current hotel details
        //---
        databaseReference.child(hotelKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Hotel hotel;
                    hotel = snapshot.getValue(Hotel.class);

                    setHotelFeatures(hotel,root);
                    populateRecyclerViewOfHotel(hotel,root);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void populateRecyclerViewOfHotel(Hotel hotel, View root) {
        double lowerPrice,higherPrice;

        lowerPrice= hotel.getPrice() - (hotel.getPrice() * 0.1);
        higherPrice =hotel.getPrice() + (hotel.getPrice() * 0.1);;

        List <String>keys = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Hotel hotelSnapshot = ds.getValue(Hotel.class);

                    if (hotelSnapshot.getPrice() > lowerPrice && hotelSnapshot.getPrice() < higherPrice && ds.getKey() != hotelKey) {
                        similarHotelKeys.add(hotelSnapshot);
                        keys.add(ds.getKey());
                    }

                }
                // Lookup the recyclerview in activity layout
                RecyclerView rvHotels = root.findViewById(R.id.similarhotels_Rv);
                // Create adapter passing in the sample user data
                adapterSimilarHotels adapter = new adapterSimilarHotels(similarHotelKeys);
                adapter.setOnItemClickListener(new adapterSimilarHotels.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("clickDetails", keys.get(position));
                        Navigation.findNavController(root).navigate(R.id.action_traveler_hotelview_self, bundle);
                    }
                });
                // Attach the adapter to the recyclerview to populate items
                rvHotels.setAdapter(adapter);
                // Set layout manager to position the items
                rvHotels.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setHotelFeatures(Hotel hotel, View root) {
        Map<String, Boolean> hotelFeature = hotel.getFeature().getFeatures();
        ArrayList <String> listFeatures = new ArrayList<>();

        // using for-each loop for iteration over Map.entrySet()
        for (Map.Entry<String,Boolean> entry : hotelFeature.entrySet()) {
            if(entry.getValue() == true)
                listFeatures.add(entry.getKey());
        }

        adapterFeatures adapterFeatures= new adapterFeatures(listFeatures);
        rvFeatures.setAdapter(adapterFeatures);
        // Set layout manager to position the items
        rvFeatures.setLayoutManager(new GridLayoutManager(getContext(),2));
    }
}
