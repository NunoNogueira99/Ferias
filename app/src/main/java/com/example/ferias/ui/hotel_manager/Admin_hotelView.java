package com.example.ferias.ui.hotel_manager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/*
import com.example.ferias.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
*/

import java.util.ArrayList;

public class Admin_hotelView extends AppCompatActivity {

    /*

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_hotel);

        pieChart = findViewById(R.id.activity_admin_pieChart);
        setupPieChart();
        loadPieChartData();
    }

    private void setupPieChart()
    {
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        //pieChart.setCenterText("Profit by season");
        //pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l= pieChart.getLegend();
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }
    private void loadPieChartData()
    {
        ArrayList<PieEntry> entries= new ArrayList<>();

        //TODO change this to actual profits entries
        entries.add(new PieEntry(0.2f, "Winter"));
        entries.add(new PieEntry(0.4f, "Summer"));
        entries.add(new PieEntry(0.3f, "Autumn"));
        entries.add(new PieEntry(0.1f, "Spring"));

        ArrayList<Integer> colors = new ArrayList<>();
        for(int color: ColorTemplate.VORDIPLOM_COLORS)
        {
            colors.add(color);
        }

        for(int color: ColorTemplate.VORDIPLOM_COLORS)
        {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Profits");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

     */
}