package com.lvdou.user.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/** 登录控制器 */
@RestController
public class LoginController {

    /** 获取登录用户名 */
    @GetMapping("/user/showLoginName")
    public Map<String,String> showName(){
        System.out.println("进来了吗？=====");
        /** 获取用户登录名 */
            String name = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
            Map<String, String> map = new HashMap<>(1);
            map.put("loginName", name);
            return map;
    }

}