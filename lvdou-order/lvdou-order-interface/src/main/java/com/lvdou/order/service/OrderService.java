package com.lvdou.order.service;

import com.lvdou.pojo.Order;
import com.lvdou.pojo.PayLog;

/**
 * 订单服务接口
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2018-08-04<p>
 */
public interface OrderService {

    /** 保存订单 */
    void saveOrder(Order order);

    /** 查询当前登录用户的支付日志 */
    PayLog findPayLogFromRedis(String userId);

    /** 修改订单状态 */
    void updateOrderStatus(String outTradeNo, String transactionId);
}
