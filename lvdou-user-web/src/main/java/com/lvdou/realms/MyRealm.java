package com.lvdou.realms;

import com.lvdou.pojo.User;
import com.lvdou.service.impl.UserServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyRealm extends AuthorizingRealm {
    
    @Autowired
    private UserServiceImpl userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("进入到了shiro的授权方法");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {

        System.out.println("进入到了shiro认证方法里");

        UsernamePasswordToken token=(UsernamePasswordToken)arg0;
        User user = userService.selectUserByUsername(token.getUsername());
        if(user==null){
            System.out.println("数据库中没有这个用户");
            return null;
        }
        return new SimpleAuthenticationInfo(user.getId(),"","");
    }
}
