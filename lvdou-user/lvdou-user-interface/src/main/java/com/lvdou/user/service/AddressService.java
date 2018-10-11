package com.lvdou.user.service;

import com.lvdou.pojo.Address;

import java.util.List;

/**
 * 地址服务接口
 */
public interface AddressService {

    /** 查询用户的收件地址 */
    List<Address> findUserAddress(String userId);
}
