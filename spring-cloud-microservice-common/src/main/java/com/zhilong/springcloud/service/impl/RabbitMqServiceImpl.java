package com.zhilong.springcloud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilong.springcloud.entity.RabbitMessage;
import com.zhilong.springcloud.service.RabbitMqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.Serializable;

public class RabbitMqServiceImpl implements RabbitMqService {

    protected static Logger logger = LoggerFactory.getLogger(RabbitMqServiceImpl.class);

    private RabbitTemplate rabbitTemplate;

    private RabbitAdmin rabbitAdmin;

    public RabbitMqServiceImpl(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void createQueueAndExchange(String queueName,String exchangeName,String routeingKey){
        Queue queue = new Queue(queueName);
        DirectExchange directExchange = new DirectExchange(exchangeName, true, false);
        this.rabbitAdmin.declareQueue(queue);
        this.rabbitAdmin.declareExchange(directExchange);
        this.rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(routeingKey));

    }

    public void sendMessage(RabbitMessage msg) {
        logger.info("Send RabbitMQ message...");
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.rabbitTemplate.convertAndSend(msg.getExchange(),msg.getRouteKey(),mapper.writeValueAsString(msg.getParams()));
        } catch (JsonProcessingException e) {
            logger.error("",e);
        }
    }

    public void sendMessageObj(RabbitMessage msg) {
        logger.info("Send RabbitMQ message...");
        this.rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if(!ack){
                logger.error("",new Exception("Message sending failure : " + cause + correlationData.toString()));
            }else{
                logger.info("Send message successfully.");
            }
        }));
        this.rabbitTemplate.convertAndSend(msg.getExchange(),msg.getRouteKey(), RabbitMessage.getSerialBytes((Serializable)msg.getParams()));
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("sender return success" + message.toString()+"==="+replyCode+"==="+replyText+"==="+routingKey);
    }
}
