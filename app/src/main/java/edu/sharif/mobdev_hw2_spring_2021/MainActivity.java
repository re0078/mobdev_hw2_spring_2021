package edu.sharif.mobdev_hw2_spring_2021;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

import edu.sharif.mobdev_hw2_spring_2021.adaptors.LocationAdaptor;
import edu.sharif.mobdev_hw2_spring_2021.db.dao.BookmarkRepository;
import edu.sharif.mobdev_hw2_spring_2021.models.LocationDTO;
import edu.sharif.mobdev_hw2_spring_2021.service.ModelConverter;
import edu.sharif.mobdev_hw2_spring_2021.services.LocationSuggestionService;
import edu.sharif.mobdev_hw2_spring_2021.ui.bookmark.BookmarkAdapter;
import edu.sharif.mobdev_hw2_spring_2021.ui.dialog.SaveBookmarkDialog;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import edu.sharif.mobdev_hw2_spring_2021.adaptors.LocationAdaptor;
import edu.sharif.mobdev_hw2_spring_2021.models.LocationDTO;
import edu.sharif.mobdev_hw2_spring_2021.services.LocationSuggestionService;
import lombok.Setter;

public class MainActivity extends AppCompatActivity implements PermissionsListener {

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int INTERNET_PERMISSION_CODE = 100;

    private MapboxMap mapboxMap;
    private MapView mapView;
    private static boolean flag_id;
    private static boolean darkModeEnabled = false;
    private SimpleSearchView simpleSearchView;
    private RecyclerView locationsRecyclerView;
    private LocationAdaptor locationAdaptor;
    private LocationSuggestionService locationService;
    private BookmarkRepository bookmarkRepository;
    private List<Feature> mapFeatures;
    private BookmarkAdapter bookmarkAdapter;
    private ModelConverter modelConverter;
    private LocationComponent locationComponent;
    @Setter
    public boolean onTrackingMode = true;
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookmarkRepository = BookmarkRepository.getInstance(getBaseContext());
        mapFeatures = new ArrayList<>();
        bookmarkAdapter = BookmarkAdapter.getInstance();
        modelConverter = ModelConverter.getInstance();
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.INTERNET, INTERNET_PERMISSION_CODE);
        setContentView(R.layout.activity_main);
        setupMapView(savedInstanceState);
        setupUserLocationButton();
        setupSearchView();
        setupSuggestionView();
        setupNavigationBar();
    }

    private void setupUserLocationButton() {
        ((FloatingActionButton) findViewById(R.id.user_location_button)).setOnClickListener(listener -> {
            onTrackingMode = true;
            updateStyle(mapboxMap);
        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void updateStyle(MapboxMap mapboxMap) {
        int styleURI;
        if (darkModeEnabled) styleURI = R.string.dark_style_uri;
        else styleURI = R.string.light_style_uri;
        Style.Builder styleBuilder = new Style.Builder().fromUri(getResources().getString(styleURI))
                .withImage(ICON_ID, BitmapFactory.decodeResource(
                        getResources(), R.drawable.mapbox_marker_icon_default))
                .withSource(new GeoJsonSource(SOURCE_ID,
                        FeatureCollection.fromFeatures(mapFeatures)))
                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(
                                iconImage(ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                        )
                );
        mapboxMap.setStyle(styleBuilder, style -> {
            if (PermissionsManager.areLocationPermissionsGranted(this)) {
                LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(this)
                        .bearingTintColor(R.color.userDeviceBearingTintColor)
                        .accuracyAlpha(1)
                        .accuracyColor(R.color.userDeviceAccuracyColor)
                        .build();

                LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions
                        .builder(this, Objects.requireNonNull(style))
                        .locationComponentOptions(locationComponentOptions)
                        .build();

                locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(locationComponentActivationOptions);

                locationComponent.setLocationComponentEnabled(true);
                if (onTrackingMode)
                    locationComponent.setCameraMode(CameraMode.TRACKING);
                else
                    locationComponent.setCameraMode(CameraMode.NONE);
                locationComponent.setRenderMode(RenderMode.COMPASS);

//                // Add the location icon click listener
//                locationComponent.addOnLocationClickListener(this); // TODO: clicking on user device pointer to show user speed would be a good ui


                locationComponent.setLocationComponentEnabled(true);
            } else {
                permissionsManager = new PermissionsManager(this);
                permissionsManager.requestLocationPermissions(this);
            }
            onTrackingMode = false;
            locationComponent.setCameraMode(CameraMode.NONE);
        });
    }

    public void setMapPoints(Point... points) {
        mapFeatures.clear();
        OptionalDouble averageLng = Arrays.stream(points).mapToDouble(Point::longitude).average();
        OptionalDouble averageLat = Arrays.stream(points).mapToDouble(Point::latitude).average();
        for (Point point : points) {
            mapFeatures.add(Feature.fromGeometry(Point.fromLngLat(point.longitude(), point.latitude())));
        }
        CameraPosition position;
        if (!averageLat.isPresent() || !averageLng.isPresent())
            position = CameraPosition.DEFAULT;
        else {
            position = new CameraPosition.Builder()
                    .target(new LatLng(averageLat.getAsDouble(), averageLng.getAsDouble()))
                    .zoom(10)
                    .tilt(30)
                    .build();
        }
        mapboxMap.setCameraPosition(position);
        updateStyle(mapboxMap);
    }

    private void setupMapView(Bundle savedInstanceState) {
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            MainActivity.this.mapboxMap = mapboxMap;
            mapboxMap.addOnMapLongClickListener(point -> {
                setMapPoints(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
                SaveBookmarkDialog saveBookmarkDialog = new SaveBookmarkDialog();
                saveBookmarkDialog.setBookmarkPoint(point);
                saveBookmarkDialog.show(getSupportFragmentManager(), "BookmarkDialog");
                return true;
            });
            setMapPoints();
        });
    }

    private void setupSearchView() {
        simpleSearchView = findViewById(R.id.searchView);
        simpleSearchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(@NotNull String query) {
                Log.d("SimpleSearchView", "Submit:" + query);
                if (!query.isEmpty() && !locationAdaptor.getLocations().isEmpty())
                    selectLocation(locationAdaptor.getLocations().get(0));

                return false;
            }

            @Override
            public boolean onQueryTextChange(@NotNull String newText) {
                Log.d("SimpleSearchView", "Text changed:" + newText);
                if (!newText.isEmpty()) {
                    List<LocationDTO> locationDTOS = locationService.getSuggestions(newText);
                    locationAdaptor.setLocations(locationDTOS);
                    locationAdaptor.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                Log.d("SimpleSearchView", "Text cleared");
                return false;
            }
        });

        simpleSearchView.setOnSearchViewListener(new SimpleSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                Log.d("SimpleSearchView", "onSearchViewShown");
                locationsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                Log.d("SimpleSearchView", "onSearchViewClosed");
                locationsRecyclerView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSearchViewShownAnimation() {
                Log.d("SimpleSearchView", "onSearchViewShownAnimation");
                locationsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosedAnimation() {
                Log.d("SimpleSearchView", "onSearchViewClosedAnimation");
                locationsRecyclerView.setVisibility(View.INVISIBLE);
            }
        });
        simpleSearchView.post(() -> simpleSearchView.showSearch());
    }

    private void selectLocation(LocationDTO locationDTO) {
        selectLocation(locationDTO.matching_place_name,
                Double.parseDouble(locationDTO.latitude),
                Double.parseDouble(locationDTO.longitude));
    }

    private void setupSuggestionView() {
        locationsRecyclerView = findViewById(R.id.location_list);
        locationsRecyclerView.setVisibility(View.INVISIBLE);
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationAdaptor = new LocationAdaptor(this);
        locationsRecyclerView.setAdapter(locationAdaptor);
        locationService = LocationSuggestionService.getInstance(getResources());
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
                locationsRecyclerView.setVisibility(View.INVISIBLE);
                simpleSearchView.post(() -> simpleSearchView.closeSearch());
                if (destination.getId() == R.id.navigation_bookmark) {
                    bookmarkAdapter.getBookmarks().clear();
                    bookmarkRepository.getBookmarks().forEach(bookmark ->
                            bookmarkAdapter.getBookmarks().add(modelConverter.getBookmarkDTO(bookmark)));
                }
            }
        });

        searchButtonView.setOnClickListener(l -> {
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.navigation_map)
                simpleSearchView.post(() -> simpleSearchView.showSearch());
        });

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setSelectedItemId(R.id.navigation_map);
        if (flag_id) {
            navView.setSelectedItemId(R.id.navigation_setting);
        }
    }

    public void selectLocation(String matchingName, double latitude, double longitude) {
        onTrackingMode = false;
        locationsRecyclerView.setVisibility(View.INVISIBLE);
        simpleSearchView.closeSearch();
        setMapPoints(Point.fromLngLat(longitude, latitude));
    }

    public void ToggleTheme(boolean isChecked) {
        flag_id = true;
        darkModeEnabled = isChecked;
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            if (requestCode == INTERNET_PERMISSION_CODE)
                Toast.makeText(MainActivity.this, "Internet Permission already granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this, "Storage Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == INTERNET_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).
                        show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).
                        show();
            }
        }
    }

        @Override
        protected void onStart () {
            super.onStart();
            mapView.onStart();
        }

        @Override
        protected void onResume () {
            super.onResume();
            mapView.onResume();
        }

        @Override
        protected void onPause () {
            super.onPause();
            mapView.onPause();
        }

        @Override
        protected void onStop () {
            super.onStop();
            mapView.onStop();
        }

        @Override
        protected void onSaveInstanceState (@NotNull Bundle outState){
            super.onSaveInstanceState(outState);
            mapView.onSaveInstanceState(outState);
        }

        @Override
        public void onLowMemory () {
            super.onLowMemory();
            mapView.onLowMemory();
        }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            mapView.onDestroy();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_device_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            updateStyle(mapboxMap);
        } else {
            Toast.makeText(this, R.string.user_device_location_permission_explanation, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}