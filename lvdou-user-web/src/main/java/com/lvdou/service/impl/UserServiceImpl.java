package com.lvdou.service.impl;

import com.lvdou.common.util.*;
import com.lvdou.mapper.SellerMapper;
import com.lvdou.mapper.UserMapper;
import com.lvdou.pojo.User;
import com.lvdou.service.IUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements IUserService {
    // 三种用户：商城后台管理用户、商家用户、商城用户(会员)
    @Resource
    private UserMapper userMapper;
    @Resource
    private SellerMapper sellerMapper;
    @Resource
    private RedisTemplate redisTemplate;

    String vcode="";  //验证码
    private String signStr="";
    private long timestamp;
    private String sign="";
    private String key="";
    //以下几个参数我曾尝试用反射读取写成配置文件，但老是乱码，最后放弃了，能用就行
    private String BaseURL="http://119.23.45.121:9999/sms/send"; //BaseURL
    private String accountKey="b282e371e3d84ca2ab05671b2294c983"; //账号
    private String accounttoken="205ec77b30d24643990468fbb050e844"; //token
    private String appId="41ae0bd57a054f068b692096bd37c62b"; //appId

    private String smsType="2";

    private String content="【绿豆传媒】您的验证码是:"; //内容

    /** 保存用户 */
    public void save(User user){
        try {
            // 设置创建时间
            user.setCreated(new Date());
            user.setUpdated(user.getCreated());
            // 设置密码MD5加密
            //user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            userMapper.insertSelective(user);
        }catch (Exception ex){
            System.out.println("保存用户异常");
            throw new RuntimeException(ex);
        }
    }


    /** 检验验证码 */
    public boolean checkSmsCode(String mobile, String smsCode){
        return smsCode.equals(redisTemplate.boundValueOps(mobile).get());
    }

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

    public Map<String, String> registerUser(User user) {
        Map<String, String> map = new HashMap<>();
        int insert = userMapper.insert(user);
        if(insert==1){
           map.put("registerResult","1");
           return map;
        }
        return null;
    }
    public Map sendValidate(String mobile) throws Exception {
        if(redisTemplate==null){
            System.out.println("redis 为空！");
            return null;
        }
        System.out.println("发送号码是："+mobile);
        timestamp = System.currentTimeMillis();
        vcode  = CommonUtils.vcode();
        System.out.println("生成的验证码是"+vcode);
        String contentStr=content+vcode;
        String encode = java.net.URLEncoder.encode(contentStr, "utf-8");
        signStr=accountKey+accounttoken+timestamp;
        signStr = new String(signStr.getBytes(),"UTF-8");

        sign= Md5Utils.MD5(signStr);  //md5加密的sign
        String keyStr=accountKey+":"+timestamp;
        keyStr = new String(keyStr.getBytes(),"UTF-8");

        key=Base64Utils.convert(keyStr);
        //base64加密的key
        //String url2 ="http://119.23.45.121:9999/sms/report/send?
        // id=b282e371e3d84ca2ab05671b2294c983
        // &sign=2c2af13589e23239ddc5f43e317bac9e
        // &key=YjI4MmUzNzFlM2Q4NGNhMmFiMDU2NzFiMjI5NGM5ODM6MTUyNjU0NDQzMjI0MQ==
        // &pageSize=500";
        String url = BaseURL+"?id="+accountKey+"&sign="+sign+
                "&key="+key+"&to="+mobile+"&appId="+appId+"&content="+encode+"&smsType="+smsType;
        System.out.println("来到了发送短信的一步了吗？");
        System.out.println("最后生成的url是："+url);
        String body = HttpClientPost.URLConnection(url, null,"UTF-8");
        redisTemplate.boundValueOps(mobile).set(vcode, 15, TimeUnit.MINUTES);

        System.out.println("来到了最后一步了吗？====");
        return null;
    }

    public User selectUserByUsername(String username) {
          return userMapper.selectUserByUsername(username);
    }

    @Override
    public Object login(String username, String password) {
        return null;
    }

    @Override
    public Object selectByName(String loginname) {
        User user = userMapper.selectUserByUsername(loginname);
        if(user!=null){
            return user;
        }
        return sellerMapper.selectBySellerName(loginname);
    }


}
