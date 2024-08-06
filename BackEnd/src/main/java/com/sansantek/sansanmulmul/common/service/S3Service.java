package com.sansantek.sansanmulmul.common.service;

import com.sansantek.sansanmulmul.common.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
@Service
public class S3Service {

    @Autowired
    private S3Uploader s3Uploader;

    // 멀티파트 파일 S3 업로드
    @Transactional
    public String uploadS3(MultipartFile file, String type) throws IOException {
        String storedFileName = "";
        if(file != null) {
            storedFileName = s3Uploader.uploadFileToS3(file, type);
        }
        return storedFileName;
    }

    // 파일 S3 업로드
    @Transactional
    public String uploadS3(File file, String type) throws IOException {
        String storedFileName = "";
        if(file != null) {
            storedFileName = s3Uploader.uploadFileToS3(file, type);
        }
        return storedFileName;
    }

    // S3 파일 삭제
    @Transactional
    public void deleteS3(String fileUrl) throws Exception {

        if(!Objects.equals(fileUrl, "")) s3Uploader.deleteS3(fileUrl);
    }
}