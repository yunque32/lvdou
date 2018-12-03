package com.lvdou.controller;

import com.lvdou.pojo.Address;
import com.lvdou.pojo.User;
import com.lvdou.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private  UserServiceImpl userService;

    /** 注册用户 */
    @PostMapping("/saveUser")
    public Map<String,Object> save(@RequestBody User user,
                                String address, String vcode){

       return userService.saveUserAndAddress(user,address,vcode);


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
    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody User user,
                        @RequestParam("vcode") String vcode){
         String phone = user.getPhone();
        Map<String, Object> map = new HashMap<>();
        //String mobileReg="^1[3|5|7|8]\\d{9}$";
             if(phone!=null&&phone.length()==11){
                 Subject subject = SecurityUtils.getSubject();

                 //2.执行认证方法
                 AuthenticationToken token = new UsernamePasswordToken(phone,
                         "");
                 try {
                     subject.login(token);

                     //把用户数据存入session（不是给shiro作为登录标记的，而是用于我们自己的业务逻辑）
                     User loginUser = (User)subject.getPrincipal();

                     int a =userService.saveUserToRedis(loginUser);
                     if(a==0){
                         System.out.println("用户存入redis成功");
                     }
                     map.put("user",user);
                     return map;
                 } catch (UnknownAccountException e) {
                     map.put("msg","账户不存在！");
                 } catch (IncorrectCredentialsException e) {
                     System.out.println("密码错误！！！！！wss");
                    map.put("msg","密码错误");
                 } catch (Exception e) {
                     System.out.println("系统错误");
                     e.printStackTrace();
                    map.put("msg","系统异常");
                 }
                 //1:代表登录成功

             }
             map.put("msg","手机号码格式有误！");
             return map;

    }
    @PostMapping("/login2")
    public Map<String,Object> login2(@RequestBody User user
                        ,@RequestParam("vcode") String vcode){
            return  userService.checkVCode(user, vcode);
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
