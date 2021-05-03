package edu.sharif.mobdev_hw2_spring_2021.adaptors;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.sharif.mobdev_hw2_spring_2021.MainActivity;
import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.models.LocationDTO;
import edu.sharif.mobdev_hw2_spring_2021.ui.view_holders.LocationViewHolder;
import lombok.Getter;
import lombok.Setter;

public class LocationAdaptor extends RecyclerView.Adapter<LocationViewHolder> {

    private final MainActivity activity;
    @Getter
    @Setter
    private List<LocationDTO> locations;

    public LocationAdaptor(Activity activity) {
        this.locations = new ArrayList<>();
        this.activity = (MainActivity) activity;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.locations_recycler_view_item, parent, false);
        view.setOnClickListener(v -> {
            activity.select_location(
                    ((TextView) v.findViewById(R.id.location_name)).getText().toString(),
                    Double.parseDouble(((TextView) v.findViewById(R.id.location_lat)).getText().toString()),
                    Double.parseDouble(((TextView) v.findViewById(R.id.location_long)).getText().toString()));
        });
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationDTO item = locations.get(position);

        holder.location_name.setText(item.name);
        holder.latitude.setText(item.lat);
        holder.longitude.setText(item.longitude);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}
