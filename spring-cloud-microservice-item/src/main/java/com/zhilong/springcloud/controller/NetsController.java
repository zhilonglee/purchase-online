package com.zhilong.springcloud.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilong.springcloud.entity.NetsTransactionPayLoad;
import com.zhilong.springcloud.service.NetsService;
import com.zhilong.springcloud.utils.EncryptAndDecryptUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sun.nio.ch.Net;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/v1/nets")
public class NetsController {


    private static Logger logger = LoggerFactory.getLogger(NetsController.class);

    @Autowired
    NetsService netsService;
    
    @PostMapping("/orderRequest")
    public ResponseEntity sendOrderRequest(@RequestBody String amount) throws JsonProcessingException {
        logger.info("---Access into /v1/nets/orderRequest---");
        return netsService.getNetsTransactionPayLoadResponseEntity(amount);
    }


    @PostMapping("/query")
    public ResponseEntity queryRequest(@RequestBody NetsTransactionPayLoad param) throws JsonProcessingException {
        logger.info("---Access into /v1/nets/query---");
        return netsService.getQueryResponseEntity(param);
    }

}
