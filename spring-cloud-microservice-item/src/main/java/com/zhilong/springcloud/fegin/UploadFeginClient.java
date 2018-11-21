package com.zhilong.springcloud.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Component
@FeignClient(name = "upload",fallback = UploadFallback.class)
public interface UploadFeginClient  {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> fileUpload(@RequestPart(value = "file",required = true) MultipartFile file);

}
