package edu.sharif.mobdev_hw2_spring_2021.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mapbox.mapboxsdk.geometry.LatLng;

import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.db.dao.BookmarkRepository;
import edu.sharif.mobdev_hw2_spring_2021.db.entity.Bookmark;
import edu.sharif.mobdev_hw2_spring_2021.ui.bookmark.BookmarkAdapter;
import edu.sharif.mobdev_hw2_spring_2021.ui.bookmark.BookmarkFragment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteBookmarkDialog extends BottomSheetDialogFragment {
    private static BookmarkRepository bookmarkRepository;
    private BookmarkAdapter adapter;
    private String bookmarkName;

    public DeleteBookmarkDialog(BookmarkAdapter adapter, String bookmarkName) {
        this.adapter = adapter;
        this.bookmarkName = bookmarkName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookmarkRepository = BookmarkRepository.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bookmark_delete_layout,
                container, false);
        Button yesButton = v.findViewById(R.id.delete_button);
        Button noButton = v.findViewById(R.id.cancel_deletion_button);
        yesButton.setOnClickListener(v1 -> {
            bookmarkRepository.deleteBookmark(bookmarkName);
            adapter.getBookmarks().removeIf(bookmarkDTO -> bookmarkDTO.getName().equals(bookmarkName));
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Bookmark deleted", Toast.LENGTH_SHORT).show();
            bookmarkName = null;
            dismiss();
        });
        noButton.setOnClickListener(v1 -> dismiss());
        return v;
    }
}
