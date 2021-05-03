package edu.sharif.mobdev_hw2_spring_2021.ui.bookmark;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import edu.sharif.mobdev_hw2_spring_2021.MainActivity;
import edu.sharif.mobdev_hw2_spring_2021.R;

public class BookmarkFragment extends Fragment {

    private BookmarkViewModel bookmarkViewModel;
    private final String TAG = "TAG-bmf";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setMapViewVisibility(false);

        bookmarkViewModel =
                new ViewModelProvider(this).get(BookmarkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        final TextView textView = root.findViewById(R.id.text_bookmark);
        bookmarkViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }
}