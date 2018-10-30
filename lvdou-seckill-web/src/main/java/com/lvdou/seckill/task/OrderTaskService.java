package com.lvdou.seckill.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.pay.service.WeixinPayService;
import com.lvdou.pojo.SeckillOrder;
import com.lvdou.seckill.service.SeckillOrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单任务服务类
 */
@Component
public class OrderTaskService {

    @Reference(timeout = 10000)
    private SeckillOrderService seckillOrderService;
    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;

    /**
     * 定时任务方法
     * cron: 触发任务调度的时间表达式 7个部分 【秒 分 小时  日  月  周 年】
     * */
    //@Scheduled(cron = "0/3 * * * * ?")
    public void closeOrderTask(){
        System.out.println("时间：" + new Date());

        /** ######## 查询超时未支付订单(Redis)  ##########*/
        List<SeckillOrder> seckillOrderList = seckillOrderService
                            .findTimeoutOrderByRedis();

        // 循环超时未支付的订单
        for (SeckillOrder seckillOrder : seckillOrderList){

            // 调用微信服务接口关闭订单
            Map<String,String> data = weixinPayService
                    .closePayTimeout(seckillOrder.getId().toString());

            // 判断是否成功
            if ("SUCCESS".equals(data.get("return_code"))){ // 成功
                System.out.println("===超时，取消订单===");
                // 删除超时未支付订单
                seckillOrderService.deleteOrderFromRedis(seckillOrder);
            }else{
                // 如果在关闭时，发生了支付(支付成功)
                if ("ORDERPAID".equals(data.get("err_code"))){
                    // 查询支付状态
                    Map<String,String> map = weixinPayService
                            .queryPayStatus(seckillOrder.getId().toString());
                    // 保存订单
                    seckillOrderService.saveOrder(seckillOrder.getUserId(),
                            map.get("transaction_id"));
                }
            }
        }
    }
}