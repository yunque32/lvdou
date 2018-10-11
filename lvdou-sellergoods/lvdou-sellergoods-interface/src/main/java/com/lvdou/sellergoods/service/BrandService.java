package com.lvdou.sellergoods.service;

import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.Brand;

import java.util.List;
import java.util.Map;


public interface BrandService {

    /** 查询品牌 */
    List<Brand> findAll();

    /**
     * 分页查询品牌
     * @param brand 品牌对象
     * @param page 当前页码
     * @param rows 每页显示的记录数
     * @return 分页实体
     */
    PageResult findByPage(Brand brand, Integer page, Integer rows);

    /** 添加品牌 */
    void saveBrand(Brand brand);

    /** 修改品牌 */
    void updateBrand(Brand brand);

    /** 批量删除 */
    void deleteBrand(Long[] ids);

    /** 查询所有的品牌(id与name) */
    List<Map<String,Object>> findBrandByIdAndName();
}
