package com.sansantek.sansanmulmul.mountain.domain.spot;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Entity
@Getter
@Setter
@NoArgsConstructor
public class MountainSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mountainSpotId;

    private Double mountainSpotLat;
    private Double mountainSpotLon;
    private Integer mountainSpotTypeId;
    private String mountainSpotType;
    private String mountainSpotDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_code", referencedColumnName = "mountain_code")
    private Mountain mountain;
}
