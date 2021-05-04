package edu.sharif.mobdev_hw2_spring_2021.ui.bookmark;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.db.dao.BookmarkRepository;
import edu.sharif.mobdev_hw2_spring_2021.db.entity.Bookmark;
import edu.sharif.mobdev_hw2_spring_2021.model.coin.BookmarkDTO;
import edu.sharif.mobdev_hw2_spring_2021.ui.dialog.DeleteBookmarkDialog;
import edu.sharif.mobdev_hw2_spring_2021.ui.dialog.SaveBookmarkDialog;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkViewHolder> {
    private static final BookmarkAdapter BOOKMARK_ADAPTER = new BookmarkAdapter(new ArrayList<>());
    @NonNull
    private final List<BookmarkDTO> bookmarks;
    private FragmentManager fragmentManager;
    private RecyclerView recyclerView;
    private Activity activity;


    public static BookmarkAdapter getInstance(RecyclerView recyclerView, Activity activity, FragmentManager fragmentManager) {
        BOOKMARK_ADAPTER.fragmentManager = fragmentManager;
        BOOKMARK_ADAPTER.recyclerView = recyclerView;
        BOOKMARK_ADAPTER.activity = activity;
        return BOOKMARK_ADAPTER;
    }

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
        holder.getDeleteIcon().setOnClickListener(v -> deleteBookmarkWithDialog(bookmark.getName()));
    }

    private void deleteBookmarkWithDialog(String bookmarkName) {
        DeleteBookmarkDialog deleteBookmarkDialog = new DeleteBookmarkDialog(this, bookmarkName);
        deleteBookmarkDialog.show(fragmentManager, "BookmarkDeletionDialog");
    }

    private void setUpScrolling() {
        AtomicBoolean loading = new AtomicBoolean(true);
        AtomicInteger pastVisibleItems = new AtomicInteger(0);
        AtomicInteger visibleItemCount = new AtomicInteger(0);
        AtomicInteger totalItemCount = new AtomicInteger(0);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount.set(layoutManager.getChildCount());
                    totalItemCount.set(layoutManager.getItemCount());
                    pastVisibleItems.set(layoutManager.findFirstVisibleItemPosition());

                    if (loading.get()) {
                        if ((visibleItemCount.get() + pastVisibleItems.get()) >= totalItemCount.get()) {
                            loading.set(false);
                            loading.set(true);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }
}
