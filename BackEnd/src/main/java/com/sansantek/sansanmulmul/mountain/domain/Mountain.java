package com.sansantek.sansanmulmul.mountain.domain;


import com.sansantek.sansanmulmul.mountain.domain.spot.MountainSpot;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
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

    private double mountain_lat; //위도
    
    private double mountain_lon; //경도

    @OneToMany(mappedBy = "mountain")
    private List<MountainSpot> mountainSpot;

}
