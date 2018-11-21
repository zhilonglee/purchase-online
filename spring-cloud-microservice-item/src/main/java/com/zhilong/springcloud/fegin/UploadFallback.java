package com.zhilong.springcloud.fegin;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
class UploadFallback implements UploadFeginClient {
    @Override
    public ResponseEntity<String> fileUpload(MultipartFile file) {
        return ResponseEntity.badRequest().body("Can not connect the upload server or connect timeout!");
    }
}