package com.zhilong.springcloud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhilong.springcloud.entity.NetsTransactionPayLoad;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface NetsService {

    ResponseEntity<NetsTransactionPayLoad> getNetsTransactionPayLoadResponseEntity(@RequestBody String amount) throws JsonProcessingException;

    ResponseEntity getQueryResponseEntity(@RequestBody NetsTransactionPayLoad param) throws JsonProcessingException;
}
