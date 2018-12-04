package com.lvdou.controller;

import com.lvdou.service.impl.ItemCatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

	@Autowired
	private  ItemCatServiceImpl itemCatService;

	/** 根据父级id查询商品分类 */
	@GetMapping("/findItemCatByParentId")
	public Map<String,Object> findItemCatByParentId(@RequestParam(value = "parentId",
			defaultValue = "0")Long parentId){
		return itemCatService.findItemCatByParentId(parentId);
	}

}