package com.lvdou.mapper;

import com.lvdou.pojo.Specification;
import com.lvdou.pojo.SpecificationOption;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecificationOptionMapper  extends Mapper<SpecificationOption>{

    /** 往规格选项表循环插入数据 */
    void save(Specification specification);

    /** 根据规格id查询规格选项 */
    @Select("select * from tb_specification_option " +
            "where spec_id = #{id} order by orders asc")
    List<SpecificationOption> findSpecOptionBySpecId(Long id);
}