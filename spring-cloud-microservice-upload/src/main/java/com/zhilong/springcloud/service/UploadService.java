package com.zhilong.springcloud.service;

import org.springframework.http.HttpEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    HttpEntity uploadFileViaSftp(MultipartFile multipartFile);
}
