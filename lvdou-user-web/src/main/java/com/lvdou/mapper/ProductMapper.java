package com.lvdou.mapper;

import com.lvdou.pojo.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ProductMapper extends Mapper<Product> {
    void deleteAll(@Param("ids") Long[] ids);

    /** 多条件查询品牌 */
    List<Product> findAll(@Param("product") Product product);

    /** 查询所有的品牌(id与name) */
    @Select("select id, product_name as text from tb_product order by id asc")
    List<Map<String,Object>> findProductByIdAndName();

    Product selectTwoIdByProductid(Long productid);

}
