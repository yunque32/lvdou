package com.lvdou.service;

import com.lvdou.common.util.Base64Utils;
import com.lvdou.common.util.FastJsonUtils;
import com.lvdou.common.util.HttpClientPost;
import com.lvdou.common.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.UUID;

//发送短信验证码
@SuppressWarnings("unused")
public class SendVCodeImpl      {

	@Autowired
	private RedisTemplate redisTemplate;

	String vcode="";  //验证码
	private String signStr="";
	private long timestamp;
	private String sign="";
	private String key="";
	private String empty="";

	@Value("${sms.url}")
	private String BaseURL; //BaseURL
	@Value("${sms.accountKey}")
	private String accountKey; //账号
	@Value("${sms.accounttoken}")
	private String accounttoken; //token
	@Value("${sms.appId}")
	private String appId; //appId

	@Value("${sms.smsType}")
	private String smsType; //smsType

	@Value("${sms.content}")
	private String content; //内容


	@SuppressWarnings("rawtypes")
	public Map sendValidate(String mobile) throws Exception {
		timestamp = System.currentTimeMillis();
		vcode  = UUID.randomUUID().toString()
				.replaceAll("-", "")
				.replaceAll("[a-z|A-Z]", "").substring(0,6);
		String contentStr=content+vcode;
		String encode = java.net.URLEncoder.encode(contentStr, "utf-8");
		signStr=accountKey+accounttoken+timestamp;
		signStr = new String(signStr.getBytes(),"UTF-8");

		sign= Md5Utils.MD5(signStr);  //md5加密的sign
		String keyStr=accountKey+":"+timestamp;
		keyStr = new String(keyStr.getBytes(),"UTF-8");

		key=Base64Utils.convert(keyStr); //base64加密的key
		//String url2 ="http://119.23.45.121:9999/sms/report/send?
		// id=b282e371e3d84ca2ab05671b2294c983
		// &sign=2c2af13589e23239ddc5f43e317bac9e
		// &key=YjI4MmUzNzFlM2Q4NGNhMmFiMDU2NzFiMjI5NGM5ODM6MTUyNjU0NDQzMjI0MQ==
		// &pageSize=500";
		String url = BaseURL+"?id="+accountKey
				+"&sign="+sign
				+"&key=" +key
				+"&to="+mobile
				+"&appId="+appId
				+"&content="+encode
				+"&smsType="+smsType;

		String body = HttpClientPost.URLConnection(url, null,"utf-8");
		Map map=FastJsonUtils.stringToCollect(body);
		return map;

	}

}
