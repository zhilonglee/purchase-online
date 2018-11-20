package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.service.UploadService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping("file")
    @ApiOperation(value = "Upload file")
    public HttpEntity fileUpload(@ApiParam(value = "File", required = true) @RequestParam(value = "file",required = true) MultipartFile file) {
        return uploadService.uploadFileViaSftp(file);
    }
}
