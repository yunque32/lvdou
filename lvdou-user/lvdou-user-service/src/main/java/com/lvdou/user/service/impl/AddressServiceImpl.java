package com.lvdou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvdou.mapper.AddressMapper;
import com.lvdou.pojo.Address;
import com.lvdou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地址服务接口实现类
 */
@Service(interfaceName = "com.lvdou.user.service.impl.AddressService")
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    /** 查询用户的收件地址 */
    public List<Address> findUserAddress(String userId){
        try{
            // 创建Address对象封装查询条件
            Address address = new Address();
            address.setUserId(userId);
            return addressMapper.select(address);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
