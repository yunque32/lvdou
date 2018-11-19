package com.lvdou.controller;

import com.lvdou.pojo.ItemCat;
import com.lvdou.sellergoods.service.impl.ItemCatServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

	  ItemCatServiceImpl itemCatService;

	/** 根据父级id查询商品分类 */
	@GetMapping("/findItemCatByParentId")
	public List<ItemCat> findItemCatByParentId(@RequestParam(value = "parentId",
			defaultValue = "0")Long parentId){

		return itemCatService.findItemCatByParentId(parentId);
	}
}