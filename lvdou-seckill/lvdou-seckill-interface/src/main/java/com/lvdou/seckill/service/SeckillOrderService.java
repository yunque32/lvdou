package com.lvdou.seckill.service;

import com.lvdou.pojo.SeckillOrder;

import java.util.List;

/**
 * 秒杀订单服务接口
 */
public interface SeckillOrderService {

    /** 保存订单到Redis */
    void submitOrderToRedis(Long id, String userId);

    /** 从Redis中获取用户的订单 */
    SeckillOrder findOrderFromRedis(String userId);

    /** 保存订单到数据库 */
    void saveOrder(String userId, String transactionId);

    /** 查询未支付订单(超时5分钟) */
    List<SeckillOrder> findTimeoutOrderByRedis();

    /** 删除超时未支付订单 */
    void deleteOrderFromRedis(SeckillOrder seckillOrder);
}
