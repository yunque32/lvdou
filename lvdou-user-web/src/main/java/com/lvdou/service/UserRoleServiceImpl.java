package com.lvdou.service;

import com.lvdou.mapper.RoleMapper;
import com.lvdou.mapper.UserRoleMapper;
import com.lvdou.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserRoleServiceImpl  {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    public List<Role> getRoleByUserId(Long id) {
        List<Integer> roleIdList = userRoleMapper.selectRoleIdByUserId(id);
        List<Role> roleList = new ArrayList<>();
        for (Integer roleid : roleIdList) {
            Role role = roleMapper.selectByPrimaryKey(roleid);
            roleList.add(role);
        }
        return roleList;
    }

}
