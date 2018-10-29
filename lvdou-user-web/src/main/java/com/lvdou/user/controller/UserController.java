package com.lvdou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.pojo.User;
import com.lvdou.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 10000)
    private UserService userService;

    /** 注册用户 */
    @PostMapping("/save")
    public boolean save(@RequestBody User user,String smsCode){
        try{
            // 检验验证码
            boolean ok = userService.checkSmsCode(user.getPhone(), smsCode);
            if (ok) {
                // 保存用户
                userService.save(user);
                return true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 发送短信验证码 */
    @GetMapping("/sendCode")
    public boolean sendCode(String phone){
        try {
            if(StringUtils.isNoneBlank(phone)){
                /** 发送验证码 */
                userService.sendSmsCode(phone);
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    @GetMapping("/checkUserName")
    public Map<String,Object> checkUserName(String userName){
        return  userService.checkUserName(userName);

    }
    @PostMapping("/registerUser")
    public Map<String,String> registerUser(User user){
        return userService.registerUser(user);
    }
}
