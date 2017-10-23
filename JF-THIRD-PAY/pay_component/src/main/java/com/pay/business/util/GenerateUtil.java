package com.pay.business.util;

import java.util.Random;

import com.core.teamwork.base.util.encrypt.MD5;

/**
 * 生成appkey和密钥
 * @author Administrator
 *
 */
public class GenerateUtil {
	//随机生成字符串+时间戳的md5
	public synchronized static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//含有字符和数字的字符串
        Random random = new Random();//随机类初始化
        StringBuffer sb = new StringBuffer();//StringBuffer类生成，为了拼接字符串
 
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);// [0,62)
 
            sb.append(str.charAt(number));
        }
        long s = System.currentTimeMillis();
        return MD5.GetMD5Code(sb.toString()+s);
    }
		
	public static void main(String[] args) {
		//appkey生成
		System.out.println(getRandomString(64));
		//密钥生成      两个md5拼起来
		System.out.println(getRandomString(64)+getRandomString(64));
	}
}
