package edu.sharif.mobdev_hw2_spring_2021.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.sharif.mobdev_hw2_spring_2021.models.LocationDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocationSuggestionService {

    private final static LocationSuggestionService LOCATION_SERVICE_INSTANCE = new LocationSuggestionService();

    public static LocationSuggestionService getInstance() {
        return LOCATION_SERVICE_INSTANCE;
    }

    public List<LocationDTO> getSuggestions(String searchText) {
        if (searchText.isEmpty()) return Collections.emptyList();

        List<LocationDTO> locationList = new ArrayList<>(Arrays.asList(
                new LocationDTO(searchText + "-1", "1.2", "2.1"),
                new LocationDTO(searchText + "-2", "3.4", "4.3"),
                new LocationDTO(searchText + "-3", "4.5", "5.4"),
                new LocationDTO(searchText + "-4", "5.6", "6.5"),
                new LocationDTO(searchText + "-5", "6.7", "7.6")
        ));
        Collections.shuffle(locationList);
        return locationList;
    }
}
