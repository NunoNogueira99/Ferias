package com.example.ferias.ui.hotel_manager.manage_hotels;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ferias.R;
import com.example.ferias.data.traveler.Booking;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Statistics extends Fragment {
    private PieChart pieChart;
    private String hotelKey;

    private Float total = Float.valueOf(0);
    private TextView total_tv;
    public Statistics(Bundle bundle) {
        hotelKey = bundle.getString("hotelKey");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.hotel_manager_hotelview_stats, container, false);


        initializeElements(root);
        getChartValues();
        clickListeners();

        return root;
    }

    private void getChartValues() {
        List<String> bookingKeys = new ArrayList<>();
        DatabaseReference databaseReferenceHotelKey = FirebaseDatabase.getInstance().getReference().child("Hotel/" + hotelKey +"/bookings");

        databaseReferenceHotelKey.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    bookingKeys.add(ds.getValue().toString());
                }
                getBookingValues(bookingKeys);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getBookingValues(List<String> bookingKeys) {
        List<Booking> bookingList = new ArrayList<>();
        DatabaseReference databaseReferenceHotelKey = FirebaseDatabase.getInstance().getReference().child("Booking/");

        databaseReferenceHotelKey.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(bookingKeys.contains(ds.getKey()))
                        bookingList.add(ds.getValue(Booking.class));
                }
                ArrangeByMonth(bookingList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ArrangeByMonth(List<Booking> bookingList) {
        LinkedHashMap<String,Float> valuesByMonth = new LinkedHashMap<>();
        List<String>months = Arrays.asList("January", " February", "March", " April", "May", "June", "July", "August", "September", "October", "November", "December");

        for(int i = 0; i<12; i++ )
        {
            valuesByMonth.put(months.get(i),Float.valueOf(0));
            for(int j=0 ;j <bookingList.size();j++)
            {
                if(bookingList.get(j).getEnterDate().getMonth() == i)
                {
                    Float value = valuesByMonth.get(months.get(i)) + bookingList.get(j).getPrice();
                    valuesByMonth.put(months.get(i),value);
                }
            }
        }

        LoadPieChart(valuesByMonth);
    }


    private void clickListeners() {
    }

    private void initializeElements(View root) {
        pieChart = root.findViewById(R.id.PieChart);
        setupPieChart();

        total_tv = root.findViewById(R.id.hotelProfits);
        total_tv.setText(String.valueOf(total));
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setCenterText("Earning by month");
        pieChart.setCenterTextSize(20);
        pieChart.getDescription().setEnabled(false);

        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);

    }
    private void LoadPieChart(LinkedHashMap<String, Float> valuesByMonth) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (LinkedHashMap.Entry<String,Float> entry : valuesByMonth.entrySet()) {
            total += entry.getValue();
            if(entry.getValue() != 0)
                entries.add(new PieEntry(entry.getValue(),entry.getKey()));
        }

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.PASTEL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Months");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);

        total_tv.setText(String.valueOf(total));
    }
}
