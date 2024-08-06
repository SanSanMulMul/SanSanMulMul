package com.sansantek.sansanmulmul.mountain.service;

import com.sansantek.sansanmulmul.common.service.S3Service;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import com.sansantek.sansanmulmul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {
    private final UserService userService;
    private final S3Service s3Service;
    private final MountainRepository mountainRepository;

    @Transactional
    public Boolean updateMountainImage(int mountainCode, MultipartFile image) throws IOException {
        Mountain mountain = mountainRepository.findByMountainCode(mountainCode);
        String imgUrl = "";
        if (image != null) {
            imgUrl = s3Service.uploadS3(image, "images");

            return true;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
