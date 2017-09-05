package com.pay.business.util.hfbpay;

import java.security.MessageDigest;

public class Md5Tools {

	public static String MD5(String s){
		char[] hexDigits={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};//十六进制
		try{
			byte[] btInput=s.getBytes(WeiXinDataHelper.UTF8Encode);//与汇付宝编码一致
			MessageDigest mdInst=MessageDigest.getInstance("MD5");//获得MD5摘要算法的messageDigest对象
			mdInst.update(btInput);//使用指定的字节更新摘要
			byte[] md=mdInst.digest();//获得密文
			//把密文转换成十六进制的字符串形式
			int j=md.length;
			char str[]=new char[j*2];
			int k=0;
			for(int i=0;i<j;i++){
				byte byte0=md[i];
				str[k++]=hexDigits[byte0>>>4& 0xf];
				str[k++]=hexDigits[byte0 & 0xf];
			}
			return new String(str);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
