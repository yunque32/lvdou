package com.lvdou.mapper;

import com.lvdou.pojo.Fruit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface FruitMapper extends Mapper<Fruit> {
    void deleteAll(@Param("ids") Long[] ids);

    /** 多条件查询品牌 */
    List<Fruit> findAll(@Param("fruit") Fruit fruit);

    /** 查询所有的品牌(id与name) */
    @Select("select id, fruit_name as text from tb_fruit order by id asc")
    List<Map<String,Object>> findFruitByIdAndName();
}
