package com.lvdou.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 */
@RestController
public class SmsController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping("/send")
    public void sendSms(){
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", "15914264552");
        map.put("templateCode", "SMS_11480310");
        map.put("signName", "五子连珠");
        map.put("message", "{\"number\":\"102931\"}");
        // 发送消息
        jmsTemplate.convertAndSend("sms", map);
    }
}
