package com.sansantek.sansanmulmul.user.domain.badge;

import java.io.Serializable;
import java.util.Objects;

public class UserBadgeId implements Serializable {
    private int user;
    private int badge;

    // 기본 생성자, equals, hashCode 구현
    public UserBadgeId() {}

    public UserBadgeId(int user, int badge) {
        this.user = user;
        this.badge = badge;
    }

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