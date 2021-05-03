package edu.sharif.mobdev_hw2_spring_2021.model.coin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class BookmarkDTO {
    private Long dbId;
    @NonNull
    private String name;
    @NonNull
    private String longitude;
    @NonNull
    private String latitude;
}

