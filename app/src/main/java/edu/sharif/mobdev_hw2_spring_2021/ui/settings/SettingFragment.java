package edu.sharif.mobdev_hw2_spring_2021.ui.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import edu.sharif.mobdev_hw2_spring_2021.MainActivity;
import edu.sharif.mobdev_hw2_spring_2021.R;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private final String TAG = "TAG-sf";
    private SwitchCompat switchCompat;
    private static int flag;
    private AlertDialog.Builder builder;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setMapViewVisibility(false);

        settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        final TextView textView = root.findViewById(R.id.text_setting);
        settingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        SwitchCompat switchTheme = root.findViewById(R.id.switchTheme);
        if (flag == 1) {
            switchTheme.setChecked(true);
        }

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //   switchDark(getView(), isChecked);
            if (isChecked) {
                // switchDark(getView(), isChecked);
                // getResources().getColor(R.color.colorPrimary);
                flag = 1;
                ((MainActivity) getActivity()).ToggleTheme(true);

                Log.d("turn_on", "amin");
            } else {
                flag = 0;
                ((MainActivity) getActivity()).ToggleTheme(false);
                Log.d("turn_off", "ali");
            }
        });

        builder = new AlertDialog.Builder(root.getContext());
        Button delete_button = root.findViewById(R.id.cache_button);
        delete_button.setOnClickListener(v -> {
            builder
                    .setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title)
                    .setCancelable(true)
                    .setPositiveButton("DELETE", (dialog, which) -> {

                    })
                    .setNegativeButton("CANCEL", (dialog, which) -> {
                        dialog.cancel();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        });


        return root;
    }
}