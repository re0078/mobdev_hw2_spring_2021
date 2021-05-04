package edu.sharif.mobdev_hw2_spring_2021;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

import edu.sharif.mobdev_hw2_spring_2021.db.dao.BookmarkRepository;
import edu.sharif.mobdev_hw2_spring_2021.service.ModelConverter;
import edu.sharif.mobdev_hw2_spring_2021.ui.bookmark.BookmarkAdapter;
import edu.sharif.mobdev_hw2_spring_2021.ui.dialog.SaveBookmarkDialog;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class MainActivity extends AppCompatActivity {

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    private MapboxMap mapboxMap;
    private MapView mapView;
    private SimpleSearchView simpleSearchView;
    private BookmarkRepository bookmarkRepository;
    private List<Feature> mapFeatures;
    private BookmarkAdapter bookmarkAdapter;
    private ModelConverter modelConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookmarkRepository = BookmarkRepository.getInstance(getBaseContext());
        mapFeatures = new ArrayList<>();
        bookmarkAdapter = BookmarkAdapter.getInstance();
        modelConverter = ModelConverter.getInstance();
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_main);
        setupMapView(savedInstanceState);
        setupSearchView();
        setupNavigationBar();
    }

    private void updateStyle(MapboxMap mapboxMap) {
        mapboxMap.setStyle(new Style.Builder().fromUri(getResources().getString(R.string.style_uri))
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
                ), style -> {
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
        mapViewAsyncAttitude();
    }

    private void mapViewAsyncAttitude() {
        mapView.getMapAsync(mapboxMap -> {
            MainActivity.this.mapboxMap = mapboxMap;
            mapboxMap.addOnMapClickListener(point -> {
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
                if (destination.getId() == R.id.navigation_bookmark) {
                    bookmarkAdapter.getBookmarks().clear();
                    bookmarkRepository.getBookmarks().forEach(bookmark ->
                            bookmarkAdapter.getBookmarks().add(modelConverter.getBookmarkDTO(bookmark)));
                }
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