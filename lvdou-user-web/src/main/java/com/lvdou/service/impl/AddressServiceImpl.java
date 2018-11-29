package com.lvdou.service.impl;

import com.lvdou.mapper.AddressMapper;
import com.lvdou.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class AddressServiceImpl  {

    @Resource
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
