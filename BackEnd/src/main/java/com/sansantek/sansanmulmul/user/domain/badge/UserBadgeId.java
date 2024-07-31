package com.sansantek.sansanmulmul.user.domain.badge;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class UserBadgeId implements Serializable {
    private int user;
    private int badge;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBadgeId that = (UserBadgeId) o;
        return Objects.equals(user, that.user) && Objects.equals(badge, that.badge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, badge);
    }
}