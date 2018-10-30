package com.lvdou.sellergoods.service;

import com.lvdou.pojo.ItemCat;

import java.util.List;

/**
 * 服务层接口
 */
public interface ItemCatService {

    /** 根据父级id查询商品分类 */
    List<ItemCat> findItemCatByParentId(Long parentId);

    /** 把商品分类数据存入Redis */
    // void saveToRedis();
}
