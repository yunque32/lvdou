package com.lvdou.realms;

import com.lvdou.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MyRealm extends AuthorizingRealm {
    
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("进入到了shiro的授权方法");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {

        System.out.println("进入到了shiro认证方法里");
        if(userMapper==null){
            System.out.println("userMapper为空！！！不能调用");
        }else if(redisTemplate==null){
            System.out.println("redis为空");
        }

//        UsernamePasswordToken token=(UsernamePasswordToken)arg0;
//        User user = userMapper.selectUserByUsername(token.getUsername());
//        if(user==null){
//            System.out.println("数据库中没有这个用户");
//            return null;
//        }
//        return new SimpleAuthenticationInfo(user.getId(),"","");
        return null;
    }
}
