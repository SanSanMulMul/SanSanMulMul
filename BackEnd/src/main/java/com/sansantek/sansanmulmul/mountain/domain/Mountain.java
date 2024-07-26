package com.sansantek.sansanmulmul.mountain.domain;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Mountain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mountain_id;

    private int mountain_code;

    private String mountain_name;

    private String mountain_location;

    private int mountain_height;

    @Column(columnDefinition = "TEXT")
    private String mountain_description;

    private String mountain_img;

    private String mountain_weather;

}
