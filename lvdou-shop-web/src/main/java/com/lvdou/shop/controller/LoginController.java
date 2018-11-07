package com.lvdou.shop.controller;

import org.apache.ibatis.annotations.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Name;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    /** 获取登录用户名 */
    @GetMapping("/user/loginName")
    public Map<String, String> loginName(){
        // 获取SecurityContext上下文对象
        SecurityContext securityContext = SecurityContextHolder.getContext();
        // 获取认证对象，再获取用户名
        String loginName = securityContext.getAuthentication().getName();

        Map<String, String> data = new HashMap<>(1);
        data.put("loginName", loginName);
        return data;
    }
}
