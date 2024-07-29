package com.sansantek.sansanmulmul.mountain.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class UserMountainId implements Serializable {
    private int user;
    private Long mountain;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMountainId that = (UserMountainId) o;
        return user == that.user && mountain.equals(that.mountain);
    }

    @Override
    public int hashCode() {
        return 31 * user + mountain.hashCode();
    }
}
