package com.sansantek.sansanmulmul.crew.domain.style;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class CrewHikingStyleId implements Serializable {
    private int group;
    private int style;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrewHikingStyleId that = (CrewHikingStyleId) o;
        return Objects.equals(group, that.group) && Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, style);
    }
}
