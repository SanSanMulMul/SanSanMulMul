package com.sansantek.sansanmulmul.user.domain.style;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class UserHikingStyleId implements Serializable {
    private int user;
    private int style;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHikingStyleId that = (UserHikingStyleId) o;
        return Objects.equals(user, that.user) && Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, style);
    }
}
