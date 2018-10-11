package com.lvdou.seckill.service;

import com.lvdou.pojo.SeckillGoods;

import java.util.List;

/**
 * 秒杀商品服务接口
 */
public interface SeckillGoodsService {

    /** 查询秒杀商品列表 */
    List<SeckillGoods> findSeckillGoods();

    /** 根据ID获取秒杀商品(从缓存中读取) */
    SeckillGoods findOneFromRedis(Long id);
}
