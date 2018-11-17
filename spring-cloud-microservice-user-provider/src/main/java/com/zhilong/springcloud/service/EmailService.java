package com.zhilong.springcloud.service;

import java.util.Map;

public interface EmailService {
    void sendMessageMail(String emailFrom, String emailTo, String title, String templateName, Map params);

}
