package edu.sharif.mobdev_hw2_spring_2021.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mapbox.mapboxsdk.maps.MapView;

public class MapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private MapView mapView;

    public MapViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }
}