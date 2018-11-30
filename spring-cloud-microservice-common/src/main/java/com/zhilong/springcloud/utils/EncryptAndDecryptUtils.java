package com.zhilong.springcloud.utils;

import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Btoa encryption algorithm
     * @param str encoding string
     * @return
     */
    public static String botaEncodePassword(String str){
        // Base64Hash String
        String base64hash = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

        // string is not empty and within the ASCII code range( \u0000 - \u00ff )
        if(str == null || isMatcher(str,"([^\\u0000-\\u00ff])")) {
            throw new Error("INVALID_CHARACTER_ERR");
        }

        int i = 0,prev=0,ascii,mod=0;
        StringBuilder result = new StringBuilder();
        while (i < str.length()) {
            // ASCII code
            ascii = str.charAt(i);
            // Group the strings into groups of three
            mod = i % 3;
            switch (mod){
                // The first 6 bits only need to shift the 8-bit binary right twice.
                case 0: result.append(base64hash.charAt(ascii>>2));
                    break;
                // The second 6 bits = the last two digits of the first 8 digits and the first four digits of the second eight digits
                case 1: result.append(base64hash.charAt((prev&3)<<4|(ascii>>4)));
                    break;
                //The third 6 bits = the last 4 digits of the second 8 digits + the first two digits of the third 8 digits
                //The fourth 6 bits = the last 6 bits of the third 8 bits
                case 2:

                    result.append(base64hash.charAt((prev & 0x0f)<<2|(ascii>>6)));
                    result.append(base64hash.charAt(ascii&0x3f));
                    break;
            }
            prev = ascii;
            i++;
        }
        // If the length of the encoded character is not a multiple of 3, it is replaced by 0, and the corresponding output character is =
        if(mod == 0){
            result.append(base64hash.charAt((prev&3)<<4));
            result.append("==");
        }else if(mod == 1){
            result.append(base64hash.charAt((prev&0x0f)<<2));
            result.append("=");
        } return result.toString();
    }

    /**
     *
     * @param str Matching string
     * @param reg Regular expression
     * @return
     */
    public static boolean isMatcher(String str,String reg){

        // Compile into a regular expression pattern
        Pattern pattern = Pattern.compile(reg);
        // Matching mode
        Matcher matcher = pattern.matcher(str); if(matcher.matches()){ return true; }
        return false;
    }
}
