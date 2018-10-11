package com.lvdou.pay.service;

import java.util.Map;

/**
 * 微信支付服务接口
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2018-08-05<p>
 */
public interface WeixinPayService {

    /** 生成支付二维码 */
    Map<String,Object> genPayCode(String outTradeNo, String totalFee);

    /** 查询支付状态 */
    Map<String,String> queryPayStatus(String outTradeNo);

    /** 关闭订单 */
    Map<String,String> closePayTimeout(String outTradeNo);
}
