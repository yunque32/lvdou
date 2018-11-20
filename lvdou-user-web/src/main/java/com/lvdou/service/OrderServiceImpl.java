package com.lvdou.service;

import com.lvdou.pojo.Cart;
import com.lvdou.common.util.IdWorker;
import com.lvdou.mapper.OrderItemMapper;
import com.lvdou.mapper.OrderMapper;
import com.lvdou.mapper.PayLogMapper;
import com.lvdou.pojo.Order;
import com.lvdou.pojo.OrderItem;
import com.lvdou.pojo.PayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl     {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayLogMapper payLogMapper;

    /** 保存订单 */
    public void saveOrder(Order order){
        try{
            // 获取当前用户的购物车数据
            List<Cart> carts = (List<Cart>)redisTemplate
                    .boundValueOps("cart_" + order.getUserId()).get();

            /** 定义订单ID集合(一次支付对应多个订单) */
            List<String> orderIdList = new ArrayList<>();
            /** 定义多个订单支付的总金额（元） */
            double totalMoney = 0;

            /**
             * 往订单表与订单明表添加数据
             * 一个商家产生一个订单
             * List<Cart> cart:一个商家的购物车数据
             * */
            for (Cart cart : carts){
                // cart产生一个订单
                Order order1 = new Order();
                // 生成一个订单id
                Long orderId = idWorker.nextId();
                // 设置订单id
                order1.setOrderId(orderId);

                // 支付类型，1、在线支付，2、货到付款
                order1.setPaymentType(order.getPaymentType());
                // 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
                order1.setStatus("1");
                // 订单创建时间
                order1.setCreateTime(new Date());
                // 订单更新时间
                order1.setUpdateTime(order1.getCreateTime());
                // 用户id
                order1.setUserId(order.getUserId());

                // 设置收件人地址
                order1.setReceiverAreaName(order.getReceiverAreaName());
                // 设置收件人手机号码
                order1.setReceiverMobile(order.getReceiverMobile());
                // 设置收件人
                order1.setReceiver(order.getReceiver());
                // 设置订单来源
                order1.setSourceType(order.getSourceType());
                // 设置商家id
                order1.setSellerId(cart.getSellerId());

                // 定义该订单的金额总计
                double money = 0.0;

                /** 循环购物车中的商品集合 */
                for (OrderItem orderItem : cart.getOrderItems()){
                    // 设置主键id
                    orderItem.setId(idWorker.nextId());
                    // 设置关联的订单id
                    orderItem.setOrderId(orderId);
                    // 设置商家id
                    orderItem.setSellerId(cart.getSellerId());
                    // 累加小计
                    money += orderItem.getTotalFee().doubleValue();

                    // 往订单明细表插入数据
                    orderItemMapper.insertSelective(orderItem);
                }

                // 添加订单id
                orderIdList.add(orderId.toString());
                // 支付总金额
                totalMoney += money;

                // 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
                order1.setPayment(new BigDecimal(money));
                // 往订单表插入数据
                orderMapper.insertSelective(order1);
            }

            // 微信扫码支付
            if ("1".equals(order.getPaymentType())){
                // 创建支付日志对象
                PayLog payLog = new PayLog();
                // 支付订单号
                payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));
                // 创建日期
                payLog.setCreateTime(new Date());
                // 支付金额（分)
                payLog.setTotalFee((long)(totalMoney * 100));
                // 用户id
                payLog.setUserId(order.getUserId());
                // 交易状态(未支付)
                payLog.setTradeState("0");
                // 订单编号列表 ['11',,'22']
                payLog.setOrderList(orderIdList.toString()
                        .replace("[","").replace("]","")
                        .replace(" ",""));
                // 支付类型
                payLog.setPayType("1");
                // 往支付日志表插入数据
                payLogMapper.insertSelective(payLog);
                // 把支付日志存储到Redis中
                redisTemplate.boundValueOps("payLog_" + order.getUserId()).set(payLog);
            }

            // 从Redis中删除该用户的购物车数据
            redisTemplate.delete("cart_" + order.getUserId());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 查询当前登录用户的支付日志 */
    public PayLog findPayLogFromRedis(String userId){
        return (PayLog)redisTemplate.boundValueOps("payLog_" + userId).get();
    }

    /** 修改订单状态 */
    public void updateOrderStatus(String outTradeNo, String transactionId){
        try{
            // 修改支付日志表
            PayLog payLog = payLogMapper.selectByPrimaryKey(outTradeNo);
            payLog.setPayTime(new Date());
            payLog.setTradeState("1"); // 已支付
            payLog.setTransactionId(transactionId);// 交易流水号
            payLogMapper.updateByPrimaryKeySelective(payLog);

            // 修改订单表
            String[] orderIds = payLog.getOrderList().split(",");
            for (String orderId : orderIds){
                Order order = new Order();
                order.setOrderId(Long.valueOf(orderId));
                order.setPaymentTime(new Date()); // 支付时间
                order.setStatus("2"); // 已付款
                orderMapper.updateByPrimaryKeySelective(order);
            }

            // 删除Redis中支付日志对象
            redisTemplate.delete("payLog_" + payLog.getUserId());

        }catch (Exception ex){
            throw  new RuntimeException(ex);
        }
    }
}