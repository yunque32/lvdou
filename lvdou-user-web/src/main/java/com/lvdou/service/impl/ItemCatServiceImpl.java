package com.lvdou.service.impl;

import com.lvdou.mapper.ItemCatMapper;
import com.lvdou.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly=false)
public class ItemCatServiceImpl{
	
	/** 注入数据访问层代理对象 */
	@Resource
	private ItemCatMapper itemCatMapper;
	@Resource
	private RedisTemplate redisTemplate;

	/** 根据父级id查询商品分类 */
	public Map<String,Object> findItemCatByParentId(Long parentId){
		System.out.println("进入到了Service方法！");
		Map<String, Object> map = new HashMap<>();
		try{
			// select * from tb_item_cat where parent_id = ?
			// 创建ItemCat对象封装查询条件
			ItemCat itemCat = new ItemCat();
			// 设置父级id
			itemCat.setParentId(parentId);
			// 查询
			List<ItemCat> ItemCats = itemCatMapper.select(itemCat);
			if(ItemCats==null){
				map.put("msg","没查出什么来！");
				return map;
			}

			map.put("ItemCats",ItemCats);
		}catch (Exception ex){

			map.put("msg","查询商品分类出现运行时异常！");
			throw new RuntimeException(ex);

		}
		return map;

	}

	/** 把商品分类数据存入Redis */
	public void saveToRedis(){
		try{
			// 查询所有的商品分类
			List<ItemCat> itemCatList = itemCatMapper.selectAll();
			for (ItemCat itemCat : itemCatList){
				redisTemplate.boundHashOps("itemCats")
						.put(itemCat.getName(), itemCat.getId());
			}
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}
}
