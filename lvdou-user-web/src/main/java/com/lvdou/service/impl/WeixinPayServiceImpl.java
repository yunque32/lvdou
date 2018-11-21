package com.lvdou.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.lvdou.common.util.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl     {

    /** 微信公众号 */
    @Value("${appid}")
    private String appid;
    /** 商户账号 */
    @Value("${partner}")
    private String partner;
    /** 商户密钥 */
    @Value("${partnerkey}")
    private String partnerkey;

    /** 统一下单请求地址 */
    @Value("${unifiedorder}")
    private String unifiedorder;
    /** 查询订单请求地址 */
    @Value("${orderquery}")
    private String orderquery;
    /** 关闭订单请求地址 */
    @Value("${closeorder}")
    private String closeorder;

    /** 生成支付二维码 */
    public Map<String,Object> genPayCode(String outTradeNo, String totalFee){
        try {
            // 定义Map集合封装请求参数
            Map<String, String> data = new HashMap<>();
            // 公众账号ID
            data.put("appid", appid);
            // 商户号	mch_id
            data.put("mch_id", partner);
            // 随机字符串	nonce_str
            data.put("nonce_str", WXPayUtil.generateNonceStr());
            // 商品描述	body
            data.put("body", "绿豆");
            // 商户订单号	out_trade_no
            data.put("out_trade_no", outTradeNo);
            // 标价金额	total_fee
            data.put("total_fee", totalFee);
            // 终端IP spbill_create_ip
            data.put("spbill_create_ip", "127.0.0.1");
            // 通知地址	notify_url
            data.put("notify_url", "http://user.lvdou.com");
            // 交易类型	trade_type NATIVE 扫码支付
            data.put("trade_type", "NATIVE");

            // 签名	sign
            /** 根据商户密钥签名生成XML格式请求参数 */
            String param = WXPayUtil.generateSignedXml(data, partnerkey);
            System.out.println("请求参数：" + param);

            // 创建HttpClientUtis对象
            HttpClientUtils httpClientUtils = new HttpClientUtils(true);
            // 发送请求，得到响应数据
            String XMLcontent = httpClientUtils.sendPost(unifiedorder, param);
            System.out.println("响应数据：" + XMLcontent);

            // 把xml格式的响应数据转化成Map集合
            Map<String,String> content = WXPayUtil.xmlToMap(XMLcontent);

            // 创建Map集合
            Map<String,Object> result = new HashMap<>();
            String code_url = content.get("code_url");
            System.out.println("微信生成的URL是"+code_url);
            result.put("codeUrl", code_url);
            result.put("outTradeNo", outTradeNo);
            result.put("totalFee", totalFee);
            return result;

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 查询支付状态 */
    public Map<String,String> queryPayStatus(String outTradeNo){
        try{

            // 定义Map集合封装请求参数
            return getXMLcontentMap(outTradeNo, orderquery);

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 关闭订单 */
    public Map<String,String> closePayTimeout(String outTradeNo){
        try{
            // 定义Map集合封装请求参数
            return getXMLcontentMap(outTradeNo, closeorder);

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private Map<String, String> getXMLcontentMap(String outTradeNo, String closeorder) throws Exception {
        Map<String, String> data = new HashMap<>();
        // 公众账号ID
        data.put("appid", appid);
        // 商户号	mch_id
        data.put("mch_id", partner);
        // 商户订单号	out_trade_no
        data.put("out_trade_no", outTradeNo);
        // 随机字符串	nonce_str
        data.put("nonce_str", WXPayUtil.generateNonceStr());

        // 签名	sign
        /** 根据商户密钥签名生成XML格式请求参数 */
        String param = WXPayUtil.generateSignedXml(data, partnerkey);
        System.out.println("请求参数：" + param);

        // 创建HttpClientUtis对象
        HttpClientUtils httpClientUtils = new HttpClientUtils(true);
        // 发送请求，得到响应数据
        String XMLcontent = httpClientUtils.sendPost(closeorder, param);
        System.out.println("响应数据：" + XMLcontent);

        // 把xml格式的响应数据转化成Map集合
        return WXPayUtil.xmlToMap(XMLcontent);
    }
}
