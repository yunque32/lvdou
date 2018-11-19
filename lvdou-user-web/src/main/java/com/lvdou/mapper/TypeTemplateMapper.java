package com.lvdou.mapper;


import com.lvdou.pojo.TypeTemplate;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TypeTemplateMapper extends Mapper<TypeTemplate> {

    /** 多条件分页查询类型模版 */
    List<TypeTemplate> findAll(@Param("typeTemplate") TypeTemplate typeTemplate);
}