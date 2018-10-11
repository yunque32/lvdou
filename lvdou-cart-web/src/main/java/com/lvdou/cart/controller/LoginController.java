package com.lvdou.cart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取登录用户名控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2018-08-02<p>
 */
@RestController
public class LoginController {

    /** 获取登录用户名 */
    @GetMapping("/user/showName")
    public Map<String,String> showName(HttpServletRequest request){

        System.out.println("=======" + request.getRequestURI());
        System.out.println("====" + request.getRequestURL());
        // 获取登录用户名
        String loginName = request.getRemoteUser();
        System.out.println("登录用户名：" + loginName);
        Map<String,String> data = new HashMap<>();
        data.put("loginName", loginName);
        return data;
    }
}
