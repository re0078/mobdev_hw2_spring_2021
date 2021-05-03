package edu.sharif.mobdev_hw2_spring_2021.clients;

import edu.sharif.mobdev_hw2_spring_2021.models.LocationDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationDTOConverter {
    private static final LocationDTOConverter MODEL_CONVERTER = new LocationDTOConverter();

    public static LocationDTOConverter getInstance() {
        return MODEL_CONVERTER;
    }

    public LocationDTO getLocationDTO(ServerLocationDTO serverLocationDTO) {
        return new LocationDTO(serverLocationDTO.getMatching_text(),
                serverLocationDTO.getMatching_place_name(),
                String.valueOf(serverLocationDTO.getCenter().get(0)),
                String.valueOf(serverLocationDTO.getCenter().get(1)));
    }
}