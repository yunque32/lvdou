package com.lvdou.service;

import com.lvdou.pojo.User;

public interface IUserService {

    Object login(String username,String password);

    Object selectByName(String loginname);
}
