package com.sansantek.sansanmulmul.mountain.controller;

import com.sansantek.sansanmulmul.mountain.DTO.WeatherResponseDto;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.service.MountainService;
import com.sansantek.sansanmulmul.util.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/mountain")
public class WeatherController {

    @Autowired
    private MountainService mountainService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather/{mountain_id}")
    @Operation(summary = "산 날씨 조회", description = "산의 위도와 경도를 사용해 일주일 날씨를 조회")
    public ResponseEntity<List<WeatherResponseDto>> getWeather(@PathVariable("mountain_id") int mountainId) {
        try {
            Mountain mountain = mountainService.getMountainDetail(mountainId);
            if (mountain == null) {
                throw new NoSuchElementException("Mountain not found");
            }

            double latitude = mountain.getMountainLat();
            double longitude = mountain.getMountainLon();

            Map<String, Object> weatherData = weatherService.getWeekWeather(latitude, longitude);
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) weatherData.get("list");

            String[] dayOfWeek = {"일", "월", "화", "수", "목", "금", "토"};
            LocalDate now = LocalDate.now();
            int dayOfWeekValue = now.getDayOfWeek().getValue();
            int dayOfMonth = now.getDayOfMonth();
            List<WeatherResponseDto> weeklyWeather = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                WeatherResponseDto dayDto = new WeatherResponseDto(dayOfWeek[(dayOfWeekValue + i) % 7], dayOfMonth + i);
                double minOfDay = Double.MAX_VALUE;
                double maxOfDay = Double.MIN_VALUE;
                double totalFeelsLike = 0;
                int totalHumidity = 0;
                double totalPop = 0;

                for (int j = 0; j < 8; j++) {
                    int index = i * 8 + j;
                    Map<String, Object> dayWeather = weatherList.get(index);
                    Map<String, Object> main = (Map<String, Object>) dayWeather.get("main");
                    double min = ((Number) main.get("temp_min")).doubleValue();
                    double max = ((Number) main.get("temp_max")).doubleValue();
                    double feelsLike = ((Number) main.get("feels_like")).doubleValue();
                    int humidity = ((Number) main.get("humidity")).intValue();
                    double pop = ((Number) dayWeather.get("pop")).doubleValue();

                    if (min < minOfDay) minOfDay = min;
                    if (max > maxOfDay) maxOfDay = max;
                    totalFeelsLike += feelsLike;
                    totalHumidity += humidity;
                    totalPop += pop;

                    if (j == 0) {
                        List<Map<String, Object>> weather = (List<Map<String, Object>>) dayWeather.get("weather");
                        Map<String, Object> weatherObj = weather.get(0);
                        dayDto.setDescription((String) weatherObj.get("icon"));
                    }
                }

                dayDto.setMin(minOfDay);
                dayDto.setMax(maxOfDay);
                dayDto.setFeelsLike(totalFeelsLike / 8);
                dayDto.setHumidity(totalHumidity / 8);
                dayDto.setPop(totalPop / 8);
                weeklyWeather.add(dayDto);
            }

            return ResponseEntity.ok(weeklyWeather);

        } catch (NoSuchElementException e) {
            log.error("산 상세 조회 실패: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("날씨 조회 실패: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
}
}
