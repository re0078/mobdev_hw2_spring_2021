package edu.sharif.mobdev_hw2_spring_2021.ui.bookmark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.db.dao.BookmarkRepository;
import edu.sharif.mobdev_hw2_spring_2021.db.entity.Bookmark;
import edu.sharif.mobdev_hw2_spring_2021.model.coin.BookmarkDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkViewHolder> {
    private static final BookmarkAdapter BOOKMARK_ADAPTER = new BookmarkAdapter(new ArrayList<>());
    private List<BookmarkDTO> bookmarks;

    public static BookmarkAdapter getInstance() {
        return BOOKMARK_ADAPTER;
    }
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
        holder.getBookmarkIcon().setImageResource(R.drawable.ic_bookmarks_list_24dp);
        holder.getBookmarkName().setText(bookmark.getName());
        holder.getLongitude().setText(bookmark.getLongitude());
        holder.getLatitude().setText(bookmark.getLatitude());
        holder.getDeleteIcon().setImageResource(R.drawable.ic_bookmarks_list_delete_24dp);
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }
}
