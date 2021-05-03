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
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.time.Duration;

import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.db.dao.BookmarkRepository;
import edu.sharif.mobdev_hw2_spring_2021.db.entity.Bookmark;
import lombok.Setter;

@Setter
public class BookmarkDialog extends BottomSheetDialogFragment {
    private static BookmarkRepository bookmarkRepository;
    private LatLng bookmarkPoint;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookmarkRepository = BookmarkRepository.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,
                container, false);

        Button saveButton = v.findViewById(R.id.save_button);

        saveButton.setOnClickListener(v1 -> {
            EditText userInput = v.findViewById(R.id.bookmark_name);
            String bookmarkName = userInput.getText().toString();
            while (bookmarkRepository.existBookmark(bookmarkName)) {
                Toast.makeText(getContext(), "A bookmark with the same name exists", Toast.LENGTH_LONG).show();
            }
            bookmarkRepository.putBookmark(new Bookmark(bookmarkName,
                    bookmarkPoint.getLongitude(), bookmarkPoint.getLatitude()));
            dismiss();
        });
        return v;
    }
}