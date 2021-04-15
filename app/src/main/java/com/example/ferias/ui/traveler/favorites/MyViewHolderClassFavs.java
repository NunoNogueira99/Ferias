package com.example.ferias.ui.traveler.favorites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView name, city, price;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.Favs_listName);
            city=itemView.findViewById(R.id.Favs_listCity);
            price=itemView.findViewById(R.id.Favs_listPrice);
            photo=itemView.findViewById(R.id.Favs_listPhoto);
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

    public MyViewHolderClassFavs(List<Hotel> hotels) {
        this.mHotels=hotels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View FavsView = inflater.inflate(R.layout.fav_list_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(FavsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = mHotels.get(position);
        holder.name.setText(hotel.getName());
        holder.city.setText(hotel.getAddress().getCity());
        holder.price.setText(Float.toString(hotel.getPrice()));

        /*Glide.with(holder.fragment)
        .load(hotel.getCoverPhoto())
        .placeholder(R.drawable.admin_backgrounf_pic)
        .fitCenter()
        .into(holder.photo);*/

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
