package com.lvdou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvdou.mapper.UserMapper;
import com.lvdou.pojo.User;
import com.lvdou.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务接口实现类
 */
@Service(interfaceName = "com.lvdou.user.service.impl.UserService")
@Transactional
public class UserServiceImpl implements UserService {
    // 三种用户：商城后台管理用户、商家用户、商城用户(会员)
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination smsQueue;
    @Value("${templateCode}")
    private String templateCode;
    @Value("${signName}")
    private String signName;

    /** 保存用户 */
    public void save(User user){
        try {
            // 设置创建时间
            user.setCreated(new Date());
            user.setUpdated(user.getCreated());
            // 设置密码MD5加密
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            userMapper.insertSelective(user);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 发送验证码 */
    public void sendSmsCode(String phone){
        try {
            System.out.println("signName:" + signName);
            // 随机生成六位数字验证码
            String code  = UUID.randomUUID().toString()
                    .replaceAll("-", "")
                    .replaceAll("[a-z|A-Z]", "").substring(0,6);
            System.out.println("code:" + code);

            // 发送验证码到用户的手机(短信发送)
            // 发送消息到消息中间件
            jmsTemplate.send(smsQueue, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("phoneNum", phone);
                    mapMessage.setString("templateCode", templateCode);
                    mapMessage.setString("signName", signName);
                    mapMessage.setString("message", "{\"number\":\""+ code +"\"}");
                    return mapMessage;
                }
            });

            // 把验证码存储到Redis(有效时间90分)
            redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 检验验证码 */
    public boolean checkSmsCode(String phone, String smsCode){
        return smsCode.equals(redisTemplate.boundValueOps(phone).get());
    }


}
