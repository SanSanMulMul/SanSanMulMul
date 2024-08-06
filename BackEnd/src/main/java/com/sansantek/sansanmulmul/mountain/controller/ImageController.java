package com.sansantek.sansanmulmul.mountain.controller;

import com.sansantek.sansanmulmul.mountain.service.ImageService;
import com.sansantek.sansanmulmul.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "이미지 Upload", description = "산,정상석 이미지 S3 upload API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mountain/img")
public class ImageController {
    private final ImageService imageService;

    @Tag(name = "사진 이미지 업로드")
    @PostMapping(value = "/{mountainCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> updateUser(@PathVariable int mountainCode, @RequestPart(value = "image") MultipartFile image)
            throws IOException {

        boolean flag = imageService.updateMountainImage(mountainCode, image);

        return ResponseEntity.ok(flag);
    }
}
