package com.zhilong.springcloud.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseEntity uploadFileViaSftp(MultipartFile multipartFile);
}
