package com.example.ferias.ui.traveler.search_hotel;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferias.R;
import com.squareup.picasso.Picasso;

public class MyViewHolderClass extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView city;
    private final TextView price;
    private ImageView image;
    private RatingBar rating;

    public MyViewHolderClass(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.search_listName);
        city=itemView.findViewById(R.id.search_listCity);
        price=itemView.findViewById(R.id.search_listPrice);
        image=itemView.findViewById(R.id.search_listPhoto);
        rating=itemView.findViewById(R.id.search_listRating);
    }

    public String getName() {
        return name.getText().toString();
    }

    public String getCity() {
        return city.getText().toString();
    }

    public String getPrice() {
        return price.getText().toString();
    }

    public Float getRating() {
        return rating.getRating();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setCity(String city) {
        this.city.setText(city);
    }

    public void setPrice(String price) {
        this.price.setText(price);
    }

    public void setRating(Float rating) {
        this.rating.setRating(rating);
    }

    public void setImage(String uri){
        Picasso.get().load(uri).into(image);
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
