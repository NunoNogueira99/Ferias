package com.example.ferias.ui.traveler.favorites;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ferias.R;
import com.example.ferias.data.hotel_manager.Hotel;

import java.util.List;
public class MyViewHolderClassFavs extends RecyclerView.Adapter<MyViewHolderClassFavs.ViewHolder>{
    private final List<Hotel> mHotels;

    public class ViewHolder extends RecyclerView.ViewHolder {
        Fragment fragment;
        TextView name, price, rating;
        ImageView photo;
        Dialog d;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.hotellist_item_name);
            price = itemView.findViewById(R.id.hotellist_item_price);
            photo = itemView.findViewById(R.id.hotellist_item_img);
            rating = itemView.findViewById(R.id.hotellist_item_rating);
        }
    }

    public MyViewHolderClassFavs(List<Hotel> hotels) {
        this.mHotels=hotels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View favoriteView = inflater.inflate(R.layout.traveler_hotellist_item, parent, false);
        favoriteView.setFocusable(true);
        favoriteView.setClickable(true);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(favoriteView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Hotel hotel = mHotels.get(position);
        holder.name.setText(hotel.getName());
        holder.price.setText(Float.toString(hotel.getPrice()));
        holder.rating.setText(Integer.toString(hotel.getRate()));

        /*
        Glide.with(holder.fragment)
        .load(hotel.getCoverPhoto())
        .placeholder(R.drawable.admin_backgrounf_pic)
        .fitCenter()
        .into(holder.photo);
        */
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("clickDetails", getRef(position).getKey());
                Navigation.findNavController(root).navigate(R.id.action_traveler_search_to_traveler_hotelview, bundle);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mHotels.size();
    }
}