package com.pay.business.util.wftpay.weChatSubscrPay.utils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import org.apache.commons.codec.digest.DigestUtils;

/**
* @Title: MD5.java 
* @Package com.pay.business.util.weChatSubscrPay.utils 
* @Description: MD5加密：此类为威富通商家提供；不作全局使用
* @author 周立波    
* @date 2017年6月27日 下午5:34:16 
* @version V1.0
*/
public class MD5 {
    public static void main(String[] args) {
        String str = "bank_type=SPDB_CREDIT&charset=UTF-8&coupon_fee=0&fee_type=CNY&is_subscribe=N&mch_id=6531000001&nonce_str=1425610810802&openid=oywgtuHmTiAuoQOUvNuu29Wbiz4o&out_trade_no=1242570188&out_transaction_id=1009790845201503060026797368&pay_result=0&result_code=0&sign_type=MD5&status=0&time_end=20150306095521&total_fee=31220&trade_type=pay.weixin.jspay&transaction_id=6531000001201503060004280625&version=2.0";
        System.out.println(MD5.sign(str, "&key=fdf8600655cf845bb5dc04d3", "utf-8"));
        
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text + key;
    	System.out.println(text);
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}