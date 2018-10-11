package com.lvdou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.pay.service.WeixinPayService;
import com.lvdou.pojo.SeckillOrder;
import com.lvdou.seckill.service.SeckillOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 秒杀订单控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2018-08-07<p>
 */
@RestController
@RequestMapping("/order")
public class SeckillOrderController {

    @Reference(timeout = 10000)
    private SeckillOrderService seckillOrderService;
    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;

    /** 秒杀下单 */
    @GetMapping("/submitOrder")
    public boolean submitOrder(Long id, HttpServletRequest request){
        try{
            // 获取当前登录用户
            String userId = request.getRemoteUser();
            // 保存订单到Redis
            seckillOrderService.submitOrderToRedis(id, userId);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }


    /** 生成微信支付二维码 */
    @GetMapping("/genPayCode")
    public Map<String, Object> genPayCode(HttpServletRequest request){
        // 获取登录用户名
        String userId = request.getRemoteUser();
        // 从Redis中获取用户的订单
        SeckillOrder seckillOrder = seckillOrderService.findOrderFromRedis(userId);
        // 支付金额（分）
        long money = (long) (seckillOrder.getMoney().doubleValue() * 100);
        // 调用微信支付服务
        Map<String,Object> data = weixinPayService.genPayCode(seckillOrder.getId().toString(),
                String.valueOf(money));
        return data;
    }

    /** 查询支付状态 */
    @GetMapping("/queryPayStatus")
    public Map<String, Integer> queryPayStatus(String outTradeNo, HttpServletRequest request){
        // 定义Map集合封装响应数据
        Map<String, Integer> result = new HashMap<>();
        result.put("status", 3);
        try {
            // 调用微信支付服务
            Map<String, String> data = weixinPayService.queryPayStatus(outTradeNo);
            // 判断支付状态码
            if ("SUCCESS".equals(data.get("trade_state"))) {
                /** 支付成功保存订单 */
                // 获取登录用户名
                String userId = request.getRemoteUser();
                // 保存订单到数据库
                seckillOrderService.saveOrder(userId, data.get("transaction_id"));
                result.put("status", 1); // 支付成功
            } else {
                result.put("status", 2); // 未支付
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;

    }
}
