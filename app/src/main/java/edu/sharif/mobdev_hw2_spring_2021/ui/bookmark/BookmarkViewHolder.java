package edu.sharif.mobdev_hw2_spring_2021.ui.bookmark;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.sharif.mobdev_hw2_spring_2021.R;
import lombok.Getter;

@Getter
public class BookmarkViewHolder extends RecyclerView.ViewHolder {
    private ImageView bookmarkIcon, deleteIcon;
    private TextView bookmarkName, latitude, longitude;

    public BookmarkViewHolder(@NonNull View itemView) {
        super(itemView);
        bookmarkIcon = itemView.findViewById(R.id.bookmark_icon);
        bookmarkName = itemView.findViewById(R.id.bookmark_name);
        longitude = itemView.findViewById(R.id.bookmark_longitude);
        latitude = itemView.findViewById(R.id.bookmark_latitude);
        deleteIcon = itemView.findViewById(R.id.bookmark_delete_icon);
    }
}
