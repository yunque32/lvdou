package com.lvdou.controller;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
//@GetMapping("/user/loginName")
//    public Map<String, String> loginName(){
//        // 获取SecurityContext上下文对象
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        // 获取认证对象，再获取用户名
//        String loginName = securityContext.getAuthentication().getName();
//
//        Map<String, String> data = new HashMap<>(1);
//        data.put("loginName", loginName);
//        return data;
//    }
    /** 获取登录用户名 */
    @GetMapping("/user/showLoginName")
    public Map<String,String> showName(HttpServletRequest request){
        Map<String, String> map = new HashMap<>(8);
        try {
            SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                    .getSession().getAttribute("SPRING_SECURITY_CONTEXT");

// 登录名
            String name = securityContextImpl.getAuthentication().getName();

            map.put("loginName", name);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("登录失败！！！");
            map.put("msg","登录失败");
        }
        return map;
        // 登录密码，未加密的
//        System.out.println("Credentials:"
//                + securityContextImpl.getAuthentication().getCredentials());
//        WebAuthenticationDetails details = (WebAuthenticationDetails) securityContextImpl
//                .getAuthentication().getDetails();
//// 获得访问地址
//        System.out.println("RemoteAddress" + details.getRemoteAddress());
//// 获得sessionid
//        System.out.println("SessionId" + details.getSessionId());
//// 获得当前用户所拥有的权限
//        List<GrantedAuthority> authorities = (List<GrantedAuthority>) securityContextImpl
//                .getAuthentication().getAuthorities();
//        for (GrantedAuthority grantedAuthority : authorities) {
//            System.out.println("Authority" + grantedAuthority.getAuthority());
//        }


    }
    @GetMapping("/sessionTimeout")
    public String sessionTimeout(){
        return "redirect:/login";

    }

}