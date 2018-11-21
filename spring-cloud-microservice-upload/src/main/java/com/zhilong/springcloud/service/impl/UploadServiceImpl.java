package com.zhilong.springcloud.service.impl;

import com.zhilong.springcloud.config.SftpConfig;
import com.zhilong.springcloud.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.zhilong.springcloud.config.SftpConfig.UploadGateway;


@Service
public class UploadServiceImpl implements UploadService {

    private static Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Autowired
    private SftpConfig sftpConfig;

    @Value("${sftp.local.directory:D:/note/}")
    private String sftpLocalDirectory;

    @Autowired
    UploadGateway uploadGateway;


    @Override
    public ResponseEntity<String> uploadFileViaSftp(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        File file = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        ResponseEntity<String> responseEntity = null;
        try {
            byte[] bytes = multipartFile.getBytes();
            File dir = new File(sftpLocalDirectory);
            if(!dir.exists() && dir.isDirectory()){
                dir.mkdirs();
            }
            file = new File(sftpLocalDirectory + originalFilename);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);

            // upload the file from local server to sftp server
            uploadGateway.upload(file);
            String path = sftpConfig.getSftpHost() + "/" + originalFilename;
            responseEntity = ResponseEntity.ok(path);

        } catch (Exception e) {
            logger.error("",e);
            responseEntity = ResponseEntity.badRequest().body(e.getMessage());
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    logger.error("",e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error("",e);
                }
            }
            // only when the IO stream all closed, can do
            // delete local file operations
            boolean delete = file.delete();
            logger.info("File in server has deleted : " + delete);
        }
        return responseEntity;
    }
}
