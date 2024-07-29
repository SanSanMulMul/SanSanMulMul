package com.sansantek.sansanmulmul.user.service;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public User loadUserByUsername(String userProviderId) {
        return userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new IllegalArgumentException((userProviderId)));
    }
}