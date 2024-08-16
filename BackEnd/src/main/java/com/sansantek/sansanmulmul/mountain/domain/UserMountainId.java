package com.sansantek.sansanmulmul.mountain.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class UserMountainId implements Serializable {
    private int user;
    private int mountain;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMountainId that = (UserMountainId) o;
        return Objects.equals(user, that.user) && Objects.equals(mountain, that.mountain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, mountain);
    }
}
