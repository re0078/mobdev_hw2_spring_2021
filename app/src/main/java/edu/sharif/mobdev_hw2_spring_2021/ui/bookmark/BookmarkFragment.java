package edu.sharif.mobdev_hw2_spring_2021.ui.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import edu.sharif.mobdev_hw2_spring_2021.R;

public class BookmarkFragment extends Fragment {

    private BookmarkViewModel bookmarkViewModel;
    private RecyclerView recyclerView;
    private final String TAG = "TAG-bmf";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookmarkViewModel = new ViewModelProvider(this).get(BookmarkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        recyclerView = root.findViewById(R.id.bookmark_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(BookmarkAdapter.getInstance(getChildFragmentManager()));
        final TextView textView = root.findViewById(R.id.text_bookmark);
        bookmarkViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() != 0)
            textView.setVisibility(View.INVISIBLE);
        return root;
    }


}