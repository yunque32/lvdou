package com.lvdou.mapper;

import com.lvdou.pojo.Goods;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface GoodsMapper extends Mapper<Goods>{

    /** 多条件查询商品 */
    List<Map<String, Object>> findAllByWhere(@Param("goods") Goods goods);

    /** 修改商品状态码 */
    void updateAuditStatus(@Param("ids") Long[] ids, @Param("status") String status);

    /** 修改商品删除状态 */
    void updateDeleteStatus(@Param("ids") Long[] ids, @Param("isDelete") String isDelete);
}