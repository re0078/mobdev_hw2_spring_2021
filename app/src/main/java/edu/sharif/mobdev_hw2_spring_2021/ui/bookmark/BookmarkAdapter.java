package edu.sharif.mobdev_hw2_spring_2021.ui.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import edu.sharif.mobdev_hw2_spring_2021.MainActivity;
import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.db.entity.Bookmark;
import edu.sharif.mobdev_hw2_spring_2021.model.coin.BookmarkDTO;
import edu.sharif.mobdev_hw2_spring_2021.service.ModelConverter;
import edu.sharif.mobdev_hw2_spring_2021.ui.dialog.DeleteBookmarkDialog;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkViewHolder> {
    private static final BookmarkAdapter BOOKMARK_ADAPTER = new BookmarkAdapter(new ArrayList<>());
    @NonNull
    private final List<BookmarkDTO> bookmarks;
    private FragmentManager fragmentManager;
    private static ModelConverter modelConverter;
    private Activity bookmarkActivity;


    public static BookmarkAdapter getInstance(Activity activity, FragmentManager fragmentManager) {
        BOOKMARK_ADAPTER.bookmarkActivity = activity;
        BOOKMARK_ADAPTER.fragmentManager = fragmentManager;
        return getInstance();
    }

    public static BookmarkAdapter getInstance() {
        modelConverter = ModelConverter.getInstance();
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
        BookmarkDTO bookmarkDTO = bookmarks.get(position);
        holder.getBookmarkIcon().setImageResource(R.drawable.ic_bookmarks_list_24dp);
        holder.getBookmarkName().setText(bookmarkDTO.getName());
        holder.getLongitude().setText(bookmarkDTO.getLongitude());
        holder.getLatitude().setText(bookmarkDTO.getLatitude());
        Bookmark bookmark = modelConverter.getBookmarkEntity(bookmarkDTO);
        holder.getBookmarkIcon().setOnClickListener(v ->
                showBookmarkOnMap(Point.fromLngLat(bookmark.getLongitude(), bookmark.getLatitude())));
        holder.getDeleteIcon().setImageResource(R.drawable.ic_bookmarks_list_delete_24dp);
        holder.getDeleteIcon().setOnClickListener(v -> deleteBookmarkWithDialog(bookmarkDTO.getName()));
    }

    private void deleteBookmarkWithDialog(String bookmarkName) {
        DeleteBookmarkDialog deleteBookmarkDialog = new DeleteBookmarkDialog(this, bookmarkName);
        deleteBookmarkDialog.show(fragmentManager, "BookmarkDeletionDialog");
    }

    private void showBookmarkOnMap(Point point) {
        ((MainActivity) bookmarkActivity).setMapPoints(point);
        ((MainActivity) bookmarkActivity).setOnTrackingMode(false);
        BottomNavigationView navView = bookmarkActivity.findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_map);
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }
}
