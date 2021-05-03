package edu.sharif.mobdev_hw2_spring_2021.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.sharif.mobdev_hw2_spring_2021.R;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        final TextView textView = root.findViewById(R.id.text_setting);
        settingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}