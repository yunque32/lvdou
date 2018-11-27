package com.lvdou.mapper;

import com.lvdou.pojo.Seller;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SellerMapper extends Mapper<Seller>{

    /** 多条件查询商家 */
    List<Seller> findAll(@Param("seller") Seller seller);

    Seller selectBySellerName(String loginname);
}