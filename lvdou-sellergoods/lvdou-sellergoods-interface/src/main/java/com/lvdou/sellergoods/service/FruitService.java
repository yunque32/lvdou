package com.lvdou.sellergoods.service;

import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.Fruit;

import java.util.List;
import java.util.Map;

public interface FruitService {
    List<Fruit> findAll();

    /**
     * 分页查询品牌
     * @param fruit 品牌对象
     * @param page 当前页码
     * @param rows 每页显示的记录数
     * @return 分页实体
     */
    PageResult findByPage(Fruit fruit, Integer page, Integer rows);

    /** 添加品牌 */
    void saveFruit(Fruit fruit);

    /** 修改品牌 */
    void updateFruit(Fruit fruit);

    /** 批量删除 */
    void deleteFruit(Long[] ids);

    /** 查询所有的品牌(id与name) */
    List<Map<String,Object>> findFruitByIdAndName();
}
