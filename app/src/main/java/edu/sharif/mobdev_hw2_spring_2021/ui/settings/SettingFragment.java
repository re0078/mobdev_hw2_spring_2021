package edu.sharif.mobdev_hw2_spring_2021.ui.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.sharif.mobdev_hw2_spring_2021.MainActivity;
import edu.sharif.mobdev_hw2_spring_2021.R;
import edu.sharif.mobdev_hw2_spring_2021.db.dao.BookmarkRepository;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private final String TAG = "TAG-sf";
    private SwitchCompat switchCompat;
    private static int flag;
    private AlertDialog.Builder builder;
    private static BookmarkRepository bookmarkRepository;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);
        bookmarkRepository = BookmarkRepository.getInstance(getContext());
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        SwitchCompat switchTheme = root.findViewById(R.id.switchTheme);
        if (flag == 1) {
            switchTheme.setChecked(true);
        }

        setUpSwitchListener(switchTheme);

        builder = new AlertDialog.Builder(root.getContext());
        Button deleteButton = root.findViewById(R.id.cache_button);
        setUpDeleteListener(deleteButton, builder);

        return root;
    }

    private void setUpSwitchListener(SwitchCompat switchCompat) {
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                flag = 1;
                ((MainActivity) getActivity()).ToggleTheme(true);
                Toast.makeText(getContext(), "Light Attracts Bugs :)", Toast.LENGTH_SHORT).show();
            } else {
                flag = 0;
                ((MainActivity) getActivity()).ToggleTheme(false);
                Toast.makeText(getContext(), "Ah SHIT, Here We Go Again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpDeleteListener(Button deleteButton, AlertDialog.Builder builder) {
        deleteButton.setOnClickListener(v -> {
            builder
                    .setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title)
                    .setCancelable(true)
                    .setPositiveButton("DELETE", (dialog, which) -> {
                        bookmarkRepository.deleteBookmarks();
                        dialog.cancel();
                        Toast.makeText(getContext(), "All Bookmarks deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("CANCEL", (dialog, which) -> {
                        dialog.cancel();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        });
    }
}