package com.example.ferias.ui.simple_user.search_hotel;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferias.R;

public class MyViewHolderClass extends RecyclerView.ViewHolder {
    TextView name, city, price;

    public MyViewHolderClass(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.search_listName);
        city=itemView.findViewById(R.id.search_listCity);
        price=itemView.findViewById(R.id.search_listPrice);
    }

    @Override
    public String toString() {
        return "MyViewHolderClass{" +
                "name=" + name +
                ", city=" + city +
                ", price=" + price +
                '}';
    }
}
