package com.lvdou.controller;

import com.lvdou.pojo.Address;
import com.lvdou.service.AddressServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/order")
public class AddressController {

    /** 查询用户的收件地址 */
    @GetMapping("/findUserAddress")
    public List<Address> findUserAddress(HttpServletRequest request){
        /** 获取登录用户名 */
        String userId = request.getRemoteUser();
        AddressServiceImpl addressService = new AddressServiceImpl();
        return addressService.findUserAddress(userId);
    }
}
