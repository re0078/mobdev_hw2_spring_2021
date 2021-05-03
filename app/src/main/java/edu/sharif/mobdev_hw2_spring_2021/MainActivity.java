package edu.sharif.mobdev_hw2_spring_2021;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private MapboxMap mapboxMap;
    private MapView mapView;
    private SimpleSearchView simpleSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_main);
        setupMapView(savedInstanceState);
        setupSearchView();
        setupNavigationBar();
    }

    private void setupMapView(Bundle savedInstanceState) {
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            MainActivity.this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
                // Map is set up and the style has loaded. Now you can add data or make other map adjustments
            });
        });
    }

    private void setupSearchView() {
        simpleSearchView = findViewById(R.id.searchView);
        simpleSearchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(@NotNull String query) {
                Log.d("SimpleSearchView", "Submit:" + query);
                mapboxMap.setCameraPosition(CameraPosition.DEFAULT);
                return false;
            }

            @Override
            public boolean onQueryTextChange(@NotNull String newText) {
                Log.d("SimpleSearchView", "Text changed:" + newText);
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                Log.d("SimpleSearchView", "Text cleared");
                return false;
            }
        });
        simpleSearchView.post(() -> simpleSearchView.showSearch());
    }

    private void setupNavigationBar() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_bookmark, R.id.navigation_map, R.id.navigation_setting)
                .build();

        View searchButtonView = findViewById(R.id.action_search);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_map) {
                mapView.setVisibility(View.VISIBLE);
                searchButtonView.setAlpha(1f);
            } else {
                searchButtonView.setAlpha(0.2f);
                mapView.setVisibility(View.INVISIBLE);
                simpleSearchView.post(() -> simpleSearchView.closeSearch());
            }
        });

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setSelectedItemId(R.id.navigation_map);

        searchButtonView.setOnClickListener(l -> {
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.navigation_map)
                simpleSearchView.post(() -> simpleSearchView.showSearch());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}