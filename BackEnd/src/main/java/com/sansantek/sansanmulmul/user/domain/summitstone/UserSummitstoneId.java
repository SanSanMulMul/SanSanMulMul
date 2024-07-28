package com.sansantek.sansanmulmul.user.domain.summitstone;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class UserSummitstoneId implements Serializable {
    private int user;
    private int summitstone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSummitstoneId that = (UserSummitstoneId) o;
        return Objects.equals(user, that.user) && Objects.equals(summitstone, that.summitstone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, summitstone);
    }
}
