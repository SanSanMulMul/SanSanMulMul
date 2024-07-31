package com.sansantek.sansanmulmul.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class RecordResonse {

    private int recordId;
    private String mountainName;
//    private LocalDateTime groupStartDate;

}
