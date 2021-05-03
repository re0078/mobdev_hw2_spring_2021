package edu.sharif.mobdev_hw2_spring_2021.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LocationDTO {
    public String matching_text, matching_place_name;
    public String longitude, latitude;
}
