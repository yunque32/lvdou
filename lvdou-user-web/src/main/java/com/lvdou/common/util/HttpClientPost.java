package com.lvdou.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("unused")
public class HttpClientPost {
	public static String URLConnection(String url, Map<String,String> map,String encoding) throws Exception{
		 String body = "";  
		  
	        //创建httpclient对象  
	        CloseableHttpClient client = HttpClients.createDefault();  
	        //创建post方式请求对象  
	        HttpPost httpPost = new HttpPost(url);  
	          
	        //装填参数  
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	        if(map!=null){  
	            for (Entry<String, String> entry : map.entrySet()) {  
	                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
	            }  
	        }  
	        //设置参数到请求对象中  
	        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));  
	  
	        //设置header信息  
	        //指定报文头【Content-type】、【User-Agent】  
	        httpPost.setHeader("Accept", "application/json");  
	        httpPost.setHeader("Content-Type", "application/json;charset=utf-8"); 
	        
	        //执行请求操作，并拿到结果（同步阻塞）  
	        CloseableHttpResponse response = client.execute(httpPost);  
	        //获取结果实体  
	        HttpEntity entity = response.getEntity();  
	        if (entity != null) {  
	            //按指定编码转换结果实体为String类型  
	            body = EntityUtils.toString(entity, encoding);  
	        }  
	        EntityUtils.consume(entity);  
	        //释放链接  
	        response.close();  
	        return body;  
	    }  
	}
