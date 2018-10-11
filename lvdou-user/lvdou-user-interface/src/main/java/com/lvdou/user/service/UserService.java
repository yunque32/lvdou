package com.lvdou.user.service;

import com.lvdou.pojo.User; /**
 * 用户服务接口
 */
public interface UserService {

    /** 保存用户 */
    void save(User user);

    /** 发送验证码 */
    void sendSmsCode(String phone);

    /** 检验验证码 */
    boolean checkSmsCode(String phone, String smsCode);
}
