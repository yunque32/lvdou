package com.lvdou.common.util;

import java.util.Random;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class CommonUtils {
	public static String vcode(){
		String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);//生成短信验证码	}
		return verifyCode;
	}
	
	
	public static boolean isMobileNO(String mobiles){ 
	
	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		  
	boolean isMobile = p.matcher(mobiles).matches(); 
	
	return isMobile;
	
	}
}
