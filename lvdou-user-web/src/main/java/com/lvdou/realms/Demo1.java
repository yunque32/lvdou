package com.lvdou.realms;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

public class Demo1 {
    public static void main (String[] args){

        IniSecurityManagerFactory factory =
                new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager instance = factory.getInstance();
        SecurityUtils.setSecurityManager(instance);

        Subject subject = SecurityUtils.getSubject();

        AuthenticationToken token =
                new UsernamePasswordToken("eric", "123456");
        try {
            subject.login(token);
            System.out.println("认证成功-----");

        }catch (UnknownAccountException e){
            System.out.println("数据库中没有这个账户");
        }catch (IncorrectCredentialsException e){
            System.out.println("密码输入错误");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("其他错误！！！！！！");
        }

    }
}
