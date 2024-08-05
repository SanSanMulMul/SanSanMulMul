package com.sansantek.sansanmulmul.crew.domain.crewuser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class CrewUserId implements Serializable {
    private int user;
    private int crew;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrewUserId that = (CrewUserId) o;
        return Objects.equals(user, that.user) && Objects.equals(crew, that.crew);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, crew);
    }
}