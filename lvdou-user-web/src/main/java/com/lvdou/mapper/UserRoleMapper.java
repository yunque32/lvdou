package com.lvdou.mapper;

import com.lvdou.pojo.UserRole;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface UserRoleMapper extends Mapper<UserRole>{


    List<Integer> selectRoleIdByUserId(Long id);
}