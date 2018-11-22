package com.lvdou.service.impl;

import com.lvdou.pojo.Seller;
import com.lvdou.service.IUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    /*@Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        *//** 创建用户角色或权限集合 *//*
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(username, "", authorities);
    }*/
    private  SellerServiceImpl sellerService;
    private IUserService userService;
    private UserRoleServiceImpl userRoleService;
    /** 根据用户名加载用户 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据商家的id查询商家
        Seller seller = sellerService.findOne(username);
        // 判断商家是否登录成功或者是已通过审核商家
        if (seller != null && seller.getStatus().equals("1")) {
            // 创建List集合封装角色与权限
            List<GrantedAuthority> authorities = new ArrayList<>();
            // 添加权限与角色数据
            authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
            //JaasAuthenticationToken
            //JaasGrantedAuthority
            return new User(username, seller.getPassword(), authorities);
        }
        return null;
    }

    public SellerServiceImpl getSellerService() {
        return sellerService;
    }
    public void setSellerService(SellerServiceImpl sellerService) {
        this.sellerService = sellerService;
    }
}
