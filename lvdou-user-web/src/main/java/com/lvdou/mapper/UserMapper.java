package com.lvdou.mapper;

import com.lvdou.pojo.User;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    User selectUserByUsername(String username);



}