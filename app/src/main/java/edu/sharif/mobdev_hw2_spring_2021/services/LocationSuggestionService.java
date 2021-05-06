package edu.sharif.mobdev_hw2_spring_2021.services;


import android.content.res.Resources;

import java.util.Collections;
import java.util.List;

import edu.sharif.mobdev_hw2_spring_2021.clients.LocationClient;
import edu.sharif.mobdev_hw2_spring_2021.models.LocationDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocationSuggestionService {

    private LocationClient client;

    private final static LocationSuggestionService LOCATION_SERVICE_INSTANCE = new LocationSuggestionService();

    public static LocationSuggestionService getInstance(Resources resources) {
        LOCATION_SERVICE_INSTANCE.client = LocationClient.getInstance(resources);
        return LOCATION_SERVICE_INSTANCE;
    }

    public List<LocationDTO> getSuggestions(String searchText) {
        String[] words = searchText.split("\\s");
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words) {
            stringBuilder.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1))
                    .append(" ");
        }
        if (searchText.isEmpty()) return Collections.emptyList();
        List<LocationDTO> locationDTOS = client.searchLocation(stringBuilder.toString());
        return locationDTOS;
    }
}
