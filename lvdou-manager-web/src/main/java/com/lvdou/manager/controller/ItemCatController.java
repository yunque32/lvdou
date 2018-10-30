package com.lvdou.manager.controller;

import com.lvdou.pojo.ItemCat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.sellergoods.service.ItemCatService;

import java.util.List;

/**
 * ItemCatController
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

	@Reference(timeout = 10000)
	private ItemCatService itemCatService;

	/** 根据父级id查询商品分类 */
	@GetMapping("/findItemCatByParentId")
	public List<ItemCat> findItemCatByParentId(@RequestParam(value = "parentId",
			defaultValue = "0")Long parentId){

		return itemCatService.findItemCatByParentId(parentId);
	}

	/** 更新缓存 */
//	@GetMapping("/updateRedis")
//	public boolean updateRedis(){
//		try{
//			itemCatService.saveToRedis();
//			return true;
//		}catch (Exception ex){
//			ex.printStackTrace();
//		}
//		return false;
//	}
}