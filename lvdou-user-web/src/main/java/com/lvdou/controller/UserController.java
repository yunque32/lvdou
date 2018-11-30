package com.lvdou.controller;

import com.lvdou.pojo.User;
import com.lvdou.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
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
    public boolean save(@RequestBody User user,String vcode){
        try{
            // 检验验证码
            boolean ok = userService.checkSmsCode(user.getPhone(), vcode);
            if (ok) {
                // 保存用户
                userService.save(user);
                return true;
            }
        }catch (Exception ex){
            System.out.println("检测验证码出现异常！");
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
    @PostMapping("/login")
    public String login(@Param("user") User user,
                        @Param("vcode") String vcode){
        //boolean ok = userService.checkSmsCode(user.getPhone(), vcode);
         String phone = "15914389603";

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
                 } catch (UnknownAccountException e) {
                    return "4";
                 } catch (IncorrectCredentialsException e) {
                     System.out.println("密码错误！！！！！wss");
                     return "5";
                 } catch (Exception e) {
                     System.out.println("系统错误");
                     e.printStackTrace();
                     return "6";
                 }
                 //1:代表登录成功
                 return "1";
             }
             return "2";
    }
    @PostMapping("/login2")
    public User login2(@Param("user") User user
                        ,@Param("vcode") String vcode){
        String phone = user.getPhone();
        if(phone==""||phone==null){
            System.out.println("手机号码为空");
            return null;
        }
        User user1 = userService.selectUserByUsername(phone);
        if(user1!=null){
            System.out.println("查出数据库用户"+phone);
            return user1;
        }else{
            System.out.println("根据号码查询不到用户");
        }
        return null;
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
