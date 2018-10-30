package com.lvdou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.pojo.Address;
import com.lvdou.user.service.AddressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/order")
public class AddressController {

    @Reference(timeout = 10000)
    private AddressService addressService;

    /** 查询用户的收件地址 */
    @GetMapping("/findUserAddress")
    public List<Address> findUserAddress(HttpServletRequest request){
        /** 获取登录用户名 */
        String userId = request.getRemoteUser();
        return addressService.findUserAddress(userId);
    }
}
