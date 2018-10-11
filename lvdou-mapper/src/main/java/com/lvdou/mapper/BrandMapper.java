package com.lvdou.mapper;

import com.lvdou.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BrandMapper extends Mapper<Brand> {

    /** 批量删除 */
    void deleteAll(@Param("ids") Long[] ids);

    /** 多条件查询品牌 */
    List<Brand> findAll(@Param("brand") Brand brand);

    /** 查询所有的品牌(id与name) */
    @Select("select id, name as text from tb_brand order by id asc")
    List<Map<String,Object>> findBrandByIdAndName();
}