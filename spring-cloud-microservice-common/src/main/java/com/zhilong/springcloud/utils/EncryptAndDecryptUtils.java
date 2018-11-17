package com.zhilong.springcloud.utils;

import org.springframework.util.DigestUtils;

public class EncryptAndDecryptUtils {

    /**
     * MD5 encrypt
     * @param str(text str)
     * @return
     */
    public static String md5DigestEncryptAsHex(String str){
        String md5DigestAsHex = null;
        if (str != null) {
            md5DigestAsHex = DigestUtils.md5DigestAsHex(str.getBytes());
        }
        return md5DigestAsHex;
    }
}
