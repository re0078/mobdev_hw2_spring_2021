package edu.sharif.mobdev_hw2_spring_2021.ui.bookmark;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.db.dao.BookmarkRepository;
import edu.sharif.mobdev_hw2_spring_2021.db.entity.Bookmark;
import edu.sharif.mobdev_hw2_spring_2021.model.coin.BookmarkDTO;

public class BookmarkFragment extends Fragment {

    private BookmarkViewModel bookmarkViewModel;
    private RecyclerView recyclerView;
    private static BookmarkAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookmarkViewModel = new ViewModelProvider(this).get(BookmarkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        recyclerView = root.findViewById(R.id.bookmark_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = BookmarkAdapter.getInstance(recyclerView, getActivity(), getChildFragmentManager());
        recyclerView.setAdapter(adapter);
        final TextView textView = root.findViewById(R.id.text_bookmark);
        final EditText editText = root.findViewById(R.id.bookmark_search_bar);
        setUpSearchListener(editText);
        bookmarkViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        if (adapter.getItemCount() != 0)
            textView.setVisibility(View.INVISIBLE);
        return root;
    }

    private void setUpSearchListener(EditText editText) {
        List<BookmarkDTO> bookmarkDTOS = new ArrayList<>(adapter.getBookmarks());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<BookmarkDTO> tempBookmarks = new ArrayList<>();
                bookmarkDTOS.forEach(bookmarkDTO -> {
                    if (bookmarkDTO.getName().toLowerCase().contains(s.toString().toLowerCase()))
                        tempBookmarks.add(bookmarkDTO);
                });
                adapter.getBookmarks().clear();
                adapter.getBookmarks().addAll(tempBookmarks);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}