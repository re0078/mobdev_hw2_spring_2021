package edu.sharif.mobdev_hw2_spring_2021.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class BookmarkDTO {
    @NonNull
    private String name;
    @NonNull
    private String longitude;
    @NonNull
    private String latitude;
}

