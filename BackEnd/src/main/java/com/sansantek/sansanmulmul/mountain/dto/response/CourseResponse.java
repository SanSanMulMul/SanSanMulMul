package com.sansantek.sansanmulmul.mountain.dto.response;


import com.sansantek.sansanmulmul.mountain.domain.course.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CourseResponse {
    private Long courseId;
    private String courseName;
    private double courseLength;
    private int courseUptime;
    private int courseDowntime;
    private Level courseLevel;
}