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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.sharif.mobdev_hw2_spring_2021.MainActivity;
import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.model.coin.BookmarkDTO;

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
        recyclerView.setAdapter(BookmarkAdapter.getInstance());
        return root;
    }


}