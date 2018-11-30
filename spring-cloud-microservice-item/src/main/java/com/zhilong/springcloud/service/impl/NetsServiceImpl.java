package com.zhilong.springcloud.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilong.springcloud.entity.NetsTransactionPayLoad;
import com.zhilong.springcloud.service.NetsService;
import com.zhilong.springcloud.utils.EncryptAndDecryptUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NetsServiceImpl implements NetsService {

    private static Logger logger = LoggerFactory.getLogger(NetsServiceImpl.class);

    public static final String ORDERREQUESTURL = "https://uat-api.nets.com.sg:9065/uat/merchantservices/qr/dynamic/v1/order/request";
    public static final String QUERYTURL =       "https://uat-api.nets.com.sg:9065/uat/merchantservices/qr/dynamic/v1/transaction/query";
    public static final String APIKEY = "9fcb54c2-ff8b-4093-9444-51bc321bbcbd";
    public static final String SECRETKEY = "057b4621-f458-45b2-a1a5-6e5a5d5c86b1";
    public static final String EXTERNALAPIKEY = "8bc63cde-2647-4a78-ac75-d5f534b56047";
    public static final String MID = "11137066800";
    public static final String TID = "37066801";
    public static final String INSTITUTIONCODE = "20000000001";
    public static final String STAN = "100001";

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<NetsTransactionPayLoad> getNetsTransactionPayLoadResponseEntity(@RequestBody String amount) throws JsonProcessingException {
        NetsTransactionPayLoad netsTransactionPayLoad = new NetsTransactionPayLoad();
        netsTransactionPayLoad.setMti("0200");
        netsTransactionPayLoad.setProcess_code("990000");
        netsTransactionPayLoad.setAmount(amount);
        netsTransactionPayLoad.setStan(STAN);

        Date date = new Date();
        SimpleDateFormat hmsDateFormat = new SimpleDateFormat("hhmmss");
        String transactionTime = hmsDateFormat.format(date);
        SimpleDateFormat monthDayDateFormat = new SimpleDateFormat("MMdd");
        String transactionDate = monthDayDateFormat.format(date);
        netsTransactionPayLoad.setTransaction_time(transactionTime);
        netsTransactionPayLoad.setTransaction_date(transactionDate);

        netsTransactionPayLoad.setEntry_mode("000");
        netsTransactionPayLoad.setCondition_code("85");
        netsTransactionPayLoad.setInstitution_code(INSTITUTIONCODE);
        netsTransactionPayLoad.setHost_mid(MID);
        netsTransactionPayLoad.setHost_tid(TID);

        Map<String,String> npxMap = new LinkedHashMap<>();
        npxMap.put("E103",TID);
        npxMap.put("E201","00000123");
        npxMap.put("E202","SGD");
        netsTransactionPayLoad.setNpx_data(npxMap);
        netsTransactionPayLoad.setGetQRCode("Y");

        List<NetsTransactionPayLoad.CommunicationData> communicationDataList = new ArrayList<NetsTransactionPayLoad.CommunicationData>() ;
        NetsTransactionPayLoad.CommunicationData communicationData = new NetsTransactionPayLoad.CommunicationData();
        communicationData.setType("https_proxy");
        communicationData.setCategory("URL");
        communicationData.setDestination("https://webhook.site/0a2078a3-74fd-461a-b84f-78d522c375e3");
        NetsTransactionPayLoad.CommunicationData.Addon addon = new NetsTransactionPayLoad.CommunicationData.Addon();
        addon.setExternal_API_keyID(EXTERNALAPIKEY);
        communicationData.setAddon(addon);
        communicationDataList.add(communicationData);
        netsTransactionPayLoad.setCommunication_data(communicationDataList);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonvalue = objectMapper.writeValueAsString(netsTransactionPayLoad);
        logger.info("NetsTransactionPayLoad : " + jsonvalue);

        HttpHeaders headers = setNetsQRHttpHeaders(jsonvalue);


        HttpEntity<String> requestEntity = new HttpEntity(jsonvalue, headers);

        ResponseEntity<NetsTransactionPayLoad> responseEntity = null;
        try {
            NetsTransactionPayLoad responsePayLoad = restTemplate.postForObject(ORDERREQUESTURL, requestEntity, NetsTransactionPayLoad.class);
            responseEntity = ResponseEntity.ok(responsePayLoad);
            // String answer = restTemplate.postForObject(ORDERREQUESTURL, requestEntity, String.class);
            // responseEntity = restTemplate.exchange(ORDERREQUESTURL, HttpMethod.POST, requestEntity, String.class);
        } catch (RestClientException e) {
            if(e instanceof HttpClientErrorException) {
                HttpClientErrorException e1 = (HttpClientErrorException) e;
                logger.error("Response Headers : " + e1.getResponseHeaders().toString());
                logger.error("Response Body : " + e1.getResponseBodyAsString());
            }

        }
        return responseEntity;
    }

    @Override
    public ResponseEntity getQueryResponseEntity(@RequestBody NetsTransactionPayLoad param) throws JsonProcessingException {
        if(StringUtils.isBlank(param.getTransaction_time()) || StringUtils.isBlank(param.getTransaction_date()) ||
                StringUtils.isBlank(param.getTxn_identifier())) {
            return  ResponseEntity.badRequest().body("Paramters are missing!");
        }

        NetsTransactionPayLoad netsTransactionPayLoad = new NetsTransactionPayLoad();
        netsTransactionPayLoad.setMti("0100");
        netsTransactionPayLoad.setProcess_code("330000");
        netsTransactionPayLoad.setStan(STAN);
        netsTransactionPayLoad.setTransaction_time(param.getTransaction_time());
        netsTransactionPayLoad.setTransaction_date(param.getTransaction_date());
        netsTransactionPayLoad.setEntry_mode("000");
        netsTransactionPayLoad.setCondition_code("85");
        netsTransactionPayLoad.setInstitution_code(INSTITUTIONCODE);
        netsTransactionPayLoad.setHost_mid(MID);
        netsTransactionPayLoad.setHost_tid(TID);
        Map<String,String> npxMap = new LinkedHashMap<>();
        npxMap.put("E103",TID);
        npxMap.put("E201","00000123");
        npxMap.put("E202","SGD");
        netsTransactionPayLoad.setNpx_data(npxMap);
        netsTransactionPayLoad.setTxn_identifier(param.getTxn_identifier());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonvalue = objectMapper.writeValueAsString(netsTransactionPayLoad);
        HttpHeaders headers = setNetsQRHttpHeaders(jsonvalue);
        HttpEntity<String> requestEntity = new HttpEntity(jsonvalue, headers);
        ResponseEntity<NetsTransactionPayLoad> responseEntity = null;

        try {

            NetsTransactionPayLoad responsePayLoad = restTemplate.postForObject(QUERYTURL, requestEntity, NetsTransactionPayLoad.class);
            responseEntity = ResponseEntity.ok(responsePayLoad);

        } catch (Exception e) {

            if(e instanceof HttpClientErrorException){

                HttpClientErrorException e1 = (HttpClientErrorException) e;
                logger.error("Response Headers : " + e1.getResponseHeaders().toString());
                logger.error("Response Body : " + e1.getResponseBodyAsString());

            }  else if(e instanceof HttpServerErrorException) {

                HttpServerErrorException e1 = (HttpServerErrorException) e;
                logger.error("Response Headers : " + e1.getResponseHeaders().toString());
                logger.error("Response Body : " + e1.getResponseBodyAsString());

            } else {

                logger.error("", e);

            }

        }

        return responseEntity;
    }


    /**
     *
     * @param jsonvalue (Payload Json string value)
     * @return
     */
    private HttpHeaders setNetsQRHttpHeaders(String jsonvalue) {
        // 1. signature = json + secretkey Concatenate payload and secret
        String signature = jsonvalue + SECRETKEY;
        logger.info("json + secretkey Concatenate payload and secret : " + signature);

        // 2. signature = sha265(signature) SHA-256 Hash
        try {
            signature = DigestUtils.sha256Hex(signature);
            //signature = new HexBinaryAdapter().marshal(digest);
            logger.info("sha265(signature) SHA-256 Hash : " + signature);

            // 3. signature = uppercase(signature) Convert to Uppercase
            // signature = StringUtils.upperCase(signature);
            // logger.info("uppercase(signature) Convert to Uppercase : " + signature);

            StringBuilder stringBuilder = new StringBuilder("");
            String[] strings = signature.split("(?<=\\G..)");
            for (String string : strings) {
                int asiiNumber = Integer.parseInt(string, 16);
                stringBuilder.append((char) asiiNumber);
            }
            logger.info("'?<=\\G..' : " + Arrays.toString(strings));
            logger.info("stringBuilder toString : " + stringBuilder.toString());

            // 4. signature = base64encode(signature) Base64 encode
            signature = EncryptAndDecryptUtils.botaEncodePassword(stringBuilder.toString());
            // signature = Base64.encodeBase64String(signature.getBytes());
            logger.info("base64encode(signature) Base64 encode : " + signature);
        } catch (Exception e) {
            e.printStackTrace();
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("KeyId",APIKEY);
        headers.set("Sign",signature);
        logger.info("Request Header : " + headers.toString());
        return headers;
    }
}
