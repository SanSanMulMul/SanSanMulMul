package com.sansantek.sansanmulmul.user.domain.badge;

import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chk_badge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChkBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chk_id", nullable = false)
    @Schema(description = "칭호 검사 고유 번호", example = "1")
    private int chkId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "chk_hanra", nullable = false)
    @Schema(description = "한라산 등산 여부 확인", example = "false")
    private boolean chkHanra;

    @Column(name = "chk_jiri", nullable = false)
    @Schema(description = "지리산 등산 여부 확인", example = "false")
    private boolean chkJiri;

    @Column(name = "chk_seolak", nullable = false)
    @Schema(description = "설악산 등산 여부 확인", example = "false")
    private boolean chkSeolak;

    @Column(name = "chk_mudeung", nullable = false)
    @Schema(description = "무등산 등산 여부 확인", example = "false")
    private boolean chkMudeung;

    @Column(name = "chk_gyeryong", nullable = false)
    @Schema(description = "계룡산 등산 여부 확인", example = "false")
    private boolean chkGyeryong;

    @Column(name = "chk_unmun", nullable = false)
    @Schema(description = "운문산 등산 여부 확인", example = "false")
    private boolean chkUnmun;

    @Column(name = "chk_gazi", nullable = false)
    @Schema(description = "가지산 등산 여부 확인", example = "false")
    private boolean chkGazi;

    @Column(name = "chk_chunhwang", nullable = false)
    @Schema(description = "천황산 등산 여부 확인", example = "false")
    private boolean chkChunhwang;

    @Column(name = "chk_jaeyak", nullable = false)
    @Schema(description = "재약산 등산 여부 확인", example = "false")
    private boolean chkJaeyak;

    @Column(name = "chk_youngchuck", nullable = false)
    @Schema(description = "영축산 등산 여부 확인", example = "false")
    private boolean chkYoungchuck;

    @Column(name = "chk_sinbool", nullable = false)
    @Schema(description = "신불산 등산 여부 확인", example = "false")
    private boolean chkSinbool;

    @Column(name = "chk_ganweol", nullable = false)
    @Schema(description = "간월산 등산 여부 확인", example = "false")
    private boolean chkGanweol;

    @Column(name = "chk_gohun", nullable = false)
    @Schema(description = "고헌산 등산 여부 확인", example = "false")
    private boolean chkGohun;

}
