package com.lvdou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvdou.common.util.Base64Utils;
import com.lvdou.common.util.FastJsonUtils;
import com.lvdou.common.util.HttpClientPost;
import com.lvdou.common.util.Md5Utils;
import com.lvdou.mapper.UserMapper;
import com.lvdou.pojo.User;
import com.lvdou.user.service.SendVCode;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service(interfaceName = "com.lvdou.user.service.UserService")
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
    String vcode="";  //验证码
    private String signStr="";
    private long timestamp;
    private String sign="";
    private String key="";
    private String empty="";

    @Value("${sms.url}")
    private String BaseURL; //BaseURL
    @Value("${sms.accountKey}")
    private String accountKey; //账号
    @Value("${sms.accounttoken}")
    private String accounttoken; //token
    @Value("${sms.appId}")
    private String appId; //appId

    @Value("${sms.smsType}")
    private String smsType; //smsType

    @Value("${sms.content}")
    private String content; //内容

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

            // 把验证码存储到Redis(有效时间5分)
            redisTemplate.boundValueOps(phone).set(code, 5, TimeUnit.SECONDS);

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 检验验证码 */
    public boolean checkSmsCode(String phone, String smsCode){
        return smsCode.equals(redisTemplate.boundValueOps(phone).get());
    }

    @Override
    public Map checkUserName(String userName) {
        HashMap<String, Object> map = new HashMap<>();

        User user = new User();
        user.setUsername(userName);
        User user1 = userMapper.selectOne(user);
        if(user1==null){
            map.put("checkResult","1");
        }else {
            map.put("checkResult","2");
        }
        return map;
    }

    @Override
    public Map<String, String> registerUser(User user) {
        Map<String, String> map = new HashMap<>();
        int insert = userMapper.insert(user);
        if(insert==1){
           map.put("registerResult","1");
           return map;
        }
        return null;
    }
    @Override
    public Map sendValidate(String mobile) throws Exception {
        timestamp = System.currentTimeMillis();
        vcode  = UUID.randomUUID().toString()
                .replaceAll("-", "")
                .replaceAll("[0-9]", "").substring(0,6);
        String contentStr=content+vcode;
        String encode = java.net.URLEncoder.encode(contentStr, "utf-8");
        signStr=accountKey+accounttoken+timestamp;
        signStr = new String(signStr.getBytes(),"UTF-8");

        sign= Md5Utils.MD5(signStr);  //md5加密的sign
        String keyStr=accountKey+":"+timestamp;
        keyStr = new String(keyStr.getBytes(),"UTF-8");

        key=Base64Utils.convert(keyStr); //base64加密的key
        //String url2 ="http://119.23.45.121:9999/sms/report/send?
        // id=b282e371e3d84ca2ab05671b2294c983
        // &sign=2c2af13589e23239ddc5f43e317bac9e
        // &key=YjI4MmUzNzFlM2Q4NGNhMmFiMDU2NzFiMjI5NGM5ODM6MTUyNjU0NDQzMjI0MQ==
        // &pageSize=500";
        String url = BaseURL+"?id="+accountKey+"&sign="+sign+"&key="+key+"&to="+mobile+"&appId="+appId+"&content="+encode+"&smsType="+smsType;

        String body = HttpClientPost.URLConnection(url, null,"utf-8");

        Map map=FastJsonUtils.stringToCollect(body);
        redisTemplate.boundValueOps(mobile).set(vcode, 5, TimeUnit.SECONDS);
        return map;

    }
}
