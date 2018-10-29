package com.lvdou.user.service;

import com.lvdou.pojo.User;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {

    /** 保存用户 */
    void save(User user);

    /** 发送验证码 */
    void sendSmsCode(String phone);

    /** 检验验证码 */
    boolean checkSmsCode(String phone, String smsCode);

    Map checkUserName(String userName);

    Map<String,String> registerUser(User user);
    Map<String,Object> sendValidate(String mobile) throws Exception;
}
