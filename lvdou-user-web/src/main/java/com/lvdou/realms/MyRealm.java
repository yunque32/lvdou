package com.lvdou.realms;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class MyRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        System.out.println("授权方法");

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {

        System.out.println("进入到了shiro的认证方法");
        String username="eric";
        String password="123456";
        Long id=10L;
        UsernamePasswordToken token=(UsernamePasswordToken)arg0;
        if(!token.getUsername().equals(username)){
            System.out.println("账户不存在");
            return null;
        }
        return new SimpleAuthenticationInfo(id,password,"");


    }
}
