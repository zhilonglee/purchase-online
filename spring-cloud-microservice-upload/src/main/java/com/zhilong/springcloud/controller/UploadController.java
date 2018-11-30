package com.zhilong.springcloud.controller;

import com.zhilong.springcloud.config.SftpConfig;
import com.zhilong.springcloud.service.UploadService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @Autowired
    SftpConfig sftpConfig;

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Upload file")
    public ResponseEntity<String> fileUpload(@ApiParam(value = "File", required = true) @RequestParam(value = "file",required = true) MultipartFile file) {
        return uploadService.uploadFileViaSftp(file);
    }

    @DeleteMapping(value = "/file")
    @ApiOperation(value = "Delete file")
    public ResponseEntity<String> fileDelete(@ApiParam(value = "File Name", required = true) @RequestParam(value = "fileName",required = true) String fileName) {
        return uploadService.deleteSftpFile(sftpConfig.getSftpRemoteDirectory(),fileName);
    }
}
