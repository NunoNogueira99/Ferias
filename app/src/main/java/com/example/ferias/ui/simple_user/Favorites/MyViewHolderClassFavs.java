package com.example.ferias.ui.simple_user.Favorites;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferias.R;
import com.example.ferias.data.hotel_manager.Hotel;

import java.util.List;
public class MyViewHolderClassFavs extends RecyclerView.Adapter<MyViewHolderClassFavs.ViewHolder>{
    private List<Hotel> mHotels;
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, city, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.Favs_listName);
            city=itemView.findViewById(R.id.Favs_listCity);
            price=itemView.findViewById(R.id.Favs_listPrice);
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
        holder.name.setText(hotel.get_Name());
        holder.city.setText(hotel.get_Address().getCity());
        holder.price.setText(Float.toString(hotel.get_Price()));
        //Picasso.get().load(model.getImageURL()).into(holder.image);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("clickDetails", getRef(position).getKey());
                Navigation.findNavController(root).navigate(R.id.action_simple_user_search_to_simple_user_hotelview, bundle);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mHotels.size();
    }
}
