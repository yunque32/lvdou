package com.lvdou.sms.listener;

import com.lvdou.sms.util.SmsSendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信发送的消息监听器
 */
@Component
public class SmsSendListener {

    @Autowired
    private SmsSendUtil smsSendUtil;

    /** 消息接收方法 */
    @JmsListener(destination = "sms")
    public void sendSms(Map<String,String> map){
        // String phoneNum, String signName, String templateCode, String message
        System.out.println("map: " + map);
        try {
            // 发送短信
            boolean success = smsSendUtil.send(
                    map.get("phoneNum"),
                    map.get("signName"),
                    map.get("templateCode"),
                    map.get("message"));

            System.out.println("短信是否发送成功：" + success);

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }
}
