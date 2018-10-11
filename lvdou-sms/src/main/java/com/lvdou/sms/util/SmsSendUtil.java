package com.lvdou.sms.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 短信发送工具类
 */
@Component
public class SmsSendUtil {

    /** 产品名称:云通信短信API产品,开发者无需替换 */
    private static final String PRODUCT = "Dysmsapi";
    /** 产品域名,开发者无需替换 */
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    // 签名KEY
    @Value("${sms.accessKeyId}")
    private String accessKeyId;
    // 签名密钥
    @Value("${sms.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 发送短信验证码方法
     * @param phoneNum 手机号码
     * @param signName 短信签名
     * @param templateCode 短信模版id
     * @param message 短信内容
     * @return true: 发送成功， false：发送失败
     */
    public boolean send(String phoneNum, String signName, String templateCode, String message){
        try {
            /** 可自助调整超时时间 */
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            /** 初始化acsClient,暂不支持region化 */
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                        accessKeyId, accessKeySecret);
            /** cn-hangzhou: 中国.杭州 */
            DefaultProfile.addEndpoint("cn-hangzhou","cn-hangzhou",
                    PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            /** 组装请求对象*/
            SendSmsRequest request = new SendSmsRequest();
            // 必填: 待发送手机号
            request.setPhoneNumbers(phoneNum);
            // 必填: 短信签名-可在短信控制台中找到
            request.setSignName(signName);
            // 必填: 短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            /**
             * 可选: 模板中的变量替换JSON串,
             * 如模板内容为"亲爱的${name},您的验证码为${code}"
             */
            request.setTemplateParam(message);
            // hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            /** 判断短信是否发送成功 */
            return sendSmsResponse.getCode() != null &&
                    sendSmsResponse.getCode().equals("OK");
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

}