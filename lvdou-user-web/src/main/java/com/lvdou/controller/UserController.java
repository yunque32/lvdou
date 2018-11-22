package com.lvdou.controller;

import com.lvdou.pojo.User;
import com.lvdou.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private  UserServiceImpl userService;

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
            if(StringUtils.isNoneBlank(phone)&&phone.length()>0){
                /** 发送验证码 */
                userService.sendValidate(phone);
            }
            return true;
        }catch (Exception ex){
            System.out.println("调用短信验证码失败！");
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

    @PostMapping("/logincheck")
    public User logincheck(User user){

        return userService.selectUserByUser(user);

    }
}
