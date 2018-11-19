package com.lvdou.service;

import com.lvdou.mapper.ItemCatMapper;
import com.lvdou.pojo.ItemCat;
import com.lvdou.sellergoods.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=false)
public class ItemCatServiceImpl implements ItemCatService {
	
	/** 注入数据访问层代理对象 */
	@Autowired
	private ItemCatMapper itemCatMapper;
//	@Autowired
//	private RedisTemplate redisTemplate;

	/** 根据父级id查询商品分类 */
	public List<ItemCat> findItemCatByParentId(Long parentId){
		try{
			// select * from tb_item_cat where parent_id = ?
			// 创建ItemCat对象封装查询条件
			ItemCat itemCat = new ItemCat();
			// 设置父级id
			itemCat.setParentId(parentId);
			// 查询
			return itemCatMapper.select(itemCat);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 把商品分类数据存入Redis */
//	public void saveToRedis(){
//		try{
//			// 查询所有的商品分类
//			List<ItemCat> itemCatList = itemCatMapper.selectAll();
//			for (ItemCat itemCat : itemCatList){
//				redisTemplate.boundHashOps("itemCats")
//						.put(itemCat.getName(), itemCat.getTypeId());
//			}
//		}catch (Exception ex){
//			throw new RuntimeException(ex);
//		}
//	}
}
