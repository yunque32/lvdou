package com.lvdou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.common.util.IdWorker;
import com.lvdou.user.service.OrderService;
import com.lvdou.user.service.WeixinPayService;
import com.lvdou.pojo.Order;
import com.lvdou.pojo.PayLog;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference(timeout = 10000)
    private OrderService orderService;
    /** 微信支付服务接口 */
    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;


    /** 保存订单 */
    @PostMapping("/save")
    public boolean saveOrder(@RequestBody Order order, HttpServletRequest request){
        try{
            // 获取登录用户名
            String userId = request.getRemoteUser();
            order.setUserId(userId);
            // 订单来源 PC端
            order.setSourceType("2");
            /// 调用服务层保存订单
            orderService.saveOrder(order);
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
        // 查询当前登录用户的支付日志
        PayLog payLog = orderService.findPayLogFromRedis(userId);
//        if(payLog==null){
//            System.out.println("payLog是个null");
//        }


        // 调用微信支付服务
        return weixinPayService.genPayCode(String.valueOf(new IdWorker().nextId()),
                String.valueOf(1));

    }
    @GetMapping("/afterPaySuccess")
    public String afterPaySuccess(){
        return "dddddd";
    }

    /** 查询支付状态 */
    @GetMapping("/queryPayStatus")
    public Map<String, Integer> queryPayStatus(String outTradeNo){
        // 定义Map集合封装响应数据
        Map<String, Integer> result = new HashMap<>();
        result.put("status", 3);
        try {
            // 调用微信支付服务
            Map<String, String> data = weixinPayService.queryPayStatus(outTradeNo);
            // 判断支付状态码
            if ("SUCCESS".equals(data.get("trade_state"))) {
                result.put("status", 1); // 支付成功
                /** 修改订单状态 */
                orderService.updateOrderStatus(outTradeNo, data.get("transaction_id"));
            } else {
                result.put("status", 2); // 未支付
            }
        }catch (Exception ex){
            System.out.println("出现异常，查询支付状态失败！");
            ex.printStackTrace();
        }
        return result;
    }

}
