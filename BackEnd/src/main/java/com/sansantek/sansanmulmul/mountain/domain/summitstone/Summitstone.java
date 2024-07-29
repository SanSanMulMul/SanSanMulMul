package com.sansantek.sansanmulmul.mountain.domain.summitstone;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "summitstone")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Summitstone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stone_id", nullable = false)
    private int stoneId;

    @OneToOne
    @JoinColumn(name = "mountain_id", nullable = false)
    private Mountain mountain; // Mountain 엔티티를 참조

    @Column(name = "stone_name", nullable = false)
    private String stoneName;

    // Mountain에 추가해야함
//    @OneToOne(mappedBy = "mountain")
//    private Summitstone summitstone; // Summitstone 엔티티를 참조

    @Column(name = "stone_img", nullable = false)
    private String stoneImg;
}
