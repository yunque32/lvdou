package com.lvdou.mapper;


import com.lvdou.pojo.Specification;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SpecificationMapper extends Mapper<Specification>{

    /** 多条件分页查询 */
    List<Specification> findAll(@Param("specification") Specification specification);

    /** 查询规格列表(id,name) */
    @Select("select id, spec_name as text from tb_specification order by id asc")
    List<Map<String,Object>> findSpecByIdAndName();
}