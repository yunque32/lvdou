package com.lvdou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.pojo.Seller;
import com.lvdou.sellergoods.service.SellerService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference(timeout = 10000)
	private SellerService sellerService;

	/** 保存商家 */
	@PostMapping("/save")
	public boolean save(@RequestBody Seller seller){
		try{
			// 创建加密对象
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			// 密码加密
			String password = passwordEncoder.encode(seller.getPassword());
			// 用这个方法就可以检查明文与密文是否匹配
			//passwordEncoder.matches()

			// 设置加密后的密码
			seller.setPassword(password);
			sellerService.saveSeller(seller);
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
}