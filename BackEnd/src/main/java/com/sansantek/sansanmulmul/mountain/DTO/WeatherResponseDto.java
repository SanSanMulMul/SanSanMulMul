package com.sansantek.sansanmulmul.mountain.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponseDto {

    public WeatherResponseDto(String dayOfWeek, int dd) {
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dd;
    }

    private String dayOfWeek; // 요일
    private int dayOfMonth; // 날짜
    private double min; // 최저온도
    private double max; // 최대온도
    private String description; // 날씨설명
    private double feelsLike; // 체감온도
    private int humidity; // 습도
    private double pop; // 강수확률
}
