package edu.sharif.mobdev_hw2_spring_2021.ui.view_holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.sharif.mobdev_hw2_spring_2021.R;

public class LocationViewHolder extends RecyclerView.ViewHolder {

    public TextView location_name, latitude, longitude;

    public LocationViewHolder(@NonNull View itemView) {
        super(itemView);
        location_name = itemView.findViewById(R.id.location_name);
        latitude = itemView.findViewById(R.id.location_lat);
        longitude = itemView.findViewById(R.id.location_long);
    }
}
