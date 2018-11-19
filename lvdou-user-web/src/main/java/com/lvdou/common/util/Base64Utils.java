package com.lvdou.common.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("unused")
public class Base64Utils {
		
		/**
		 * 加密
		 * @param pwd
		 * @return
		 */
	  public static String encodeStr(String pwd)    
	    {    
	        Base64 base64 = new Base64();
	        byte[] enbytes = base64.encodeBase64Chunked(pwd.getBytes());    
	        return new String(enbytes);    
	        
	        
	    }    
	  
	  /**
	   *编码
	   * @param tagertStr
	   * @return
	   */
	   public static String convert(String tagertStr) {  
	        byte[] value;  
	        try {  
	            value = tagertStr.getBytes(Charsets.UTF_8);
	            return new String(Base64.encodeBase64(value),Charsets.UTF_8);
	        } catch (Exception e) {  
	        }  
	        return null;  
	    } 
	  
	  /**
		 * 解密
		 * @param pwd
		 * @return
		 */
	    public static String decodeStr(String pwd)    
	    {    
	        Base64 base64 = new Base64();
	        byte[] debytes = base64.decodeBase64(new String(pwd).getBytes());    
	        return new String(debytes);    
	    }   
}
