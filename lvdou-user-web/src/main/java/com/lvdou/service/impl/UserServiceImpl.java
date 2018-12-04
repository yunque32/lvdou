package com.lvdou.service.impl;

import com.lvdou.common.util.*;
import com.lvdou.mapper.AddressMapper;
import com.lvdou.mapper.SellerMapper;
import com.lvdou.mapper.UserMapper;
import com.lvdou.pojo.Address;
import com.lvdou.pojo.User;
import com.lvdou.service.IUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private AddressMapper addressMapper;
    @Resource
    private RedisTemplate redisTemplate;

    String vcode="";  //验证码
    private String signStr="";
    private long timestamp;
    private String sign="";
    private String key="";
    //以下几个参数我曾尝试写在配置文件里用反射读取，但老是乱码，看起来是ISO8859-1
    // 最后放弃了，能用就行
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

            // 设置密码MD5加密
            //user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            userMapper.insertSelective(user);
        }catch (Exception ex){
            System.out.println("保存用户异常");
            throw new RuntimeException(ex);
        }
    }
    @Transactional
    public Map<String,Object> saveUserAndAddress(User user,String address,String vcode){

        Map<String, Object> map = checkVCode(user, vcode);

        User user1 = (User) map.get("user");
        if(user1==null){
            return map;
        }
        Date createDate = new Date();
        user1.setCreated(createDate);

        int i = userMapper.insert(user1);
        if(i==1) {
            Address add = new Address();
            add.setAddress(address);
            add.setCreateDate(createDate);
            add.setPhone(user.getPhone());
            int j = addressMapper.insert(add);
            if (j == 1) {
                System.out.println("数据库插入地址成功！");
            } else {
                System.out.println("数据库保存地址失败");
                map.put("msg", "数据库保存地址失败！");
            }
        }else {
            System.out.println("数据库保存用户失败");
            map.put("msg","数据库保存用户失败！");
        }
        return map;
    }

    /** 检验验证码 */
    public Map<String,Object> checkVCode(User user, String Vcode){
        Map<String, Object> map = new HashMap<>();
        String phone = user.getPhone();
        if(phone!=null&&phone.length()==11){
            //Vcode!=null&&Vcode.length()==6
            if(true) {
                User user1 = userMapper.selectUserByUsername(user.getPhone());
                if(user1==null){
                    //Vcode.equals(redisTemplate.boundValueOps(phone).get();
                    if (true) {

                    } else {
                        map.put("msg", "验证码输入错误");
                    }
                }else {
                    map.put("msg","此账户已经被注册！");
                }

            }else{
                map.put("msg","验证码格式有误！");
            }
        }else {
            map.put("msg","手机号码格式有误！");
        }
        return map;
    }

    public Map checkUserName(String userName) {
        HashMap<String, Object> map = new HashMap<>();

        User user = new User();
        user.setUsername(userName);
        User user1 = userMapper.selectOne(user);
        if(user1==null){
            map.put("msg","1");
        }else {
            map.put("msg","2");
        }
        return map;
    }

    public Map<String, String> registerUser(User user) {
        Map<String, String> map = new HashMap<>();
        int insert = userMapper.insert(user);
        if(insert==1){
           map.put("msg","1");
           return map;
        }
        return null;
    }
    public Map sendValidate(String phone) throws Exception {

        System.out.println("发送号码是："+phone);
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
                "&key="+key+"&to="+phone+"&appId="+appId+"&content="+encode+"&smsType="+smsType;
        System.out.println("来到了发送短信的一步了吗？");
        System.out.println("最后生成的url是："+url);
        String body = HttpClientPost.URLConnection(url, null,"UTF-8");
        redisTemplate.boundValueOps(phone).set(vcode, 15, TimeUnit.MINUTES);

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


    public int saveUserToRedis(User loginUser) {
        String phone = loginUser.getPhone();
        try {
            redisTemplate.boundValueOps(phone).set(loginUser, 30, TimeUnit.MINUTES);
        }catch (Exception e){
            System.out.println("redis保存用户发生异常");
            e.printStackTrace();
            return 1;
        }
        return 0;
    }
}
