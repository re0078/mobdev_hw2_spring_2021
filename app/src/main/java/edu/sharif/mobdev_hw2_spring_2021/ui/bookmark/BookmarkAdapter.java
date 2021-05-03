package edu.sharif.mobdev_hw2_spring_2021.ui.bookmark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.db.entity.Bookmark;
import edu.sharif.mobdev_hw2_spring_2021.model.coin.BookmarkDTO;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkViewHolder> {
    private List<BookmarkDTO> bookmarks;

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.bookmark_layout, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        BookmarkDTO bookmark = bookmarks.get(position);
        holder.getBookmarkIcon().setImageResource(R.drawable.ic_baseline_bookmark_24);
        holder.getBookmarkName().setText(bookmark.getName());
        holder.getLongitude().setText(bookmark.getLongitude());
        holder.getLongitude().setText(bookmark.getLatitude());
        holder.getDeleteIcon().setImageResource(R.drawable.ic_arrow_back_black_24dp);
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }
}
