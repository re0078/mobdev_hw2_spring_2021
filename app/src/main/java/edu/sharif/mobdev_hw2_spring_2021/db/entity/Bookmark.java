package edu.sharif.mobdev_hw2_spring_2021.db.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bookmark {
    private String name;
    private Double longitude;
    private Double latitude;
}
