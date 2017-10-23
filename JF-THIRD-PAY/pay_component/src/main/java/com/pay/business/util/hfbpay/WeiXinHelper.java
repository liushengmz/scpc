package com.pay.business.util.hfbpay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class WeiXinHelper {
	
	
	public static String signMd5(String key,Map<String,String> map){
		StringBuilder _sbString=new StringBuilder();
		_sbString.append("version="+map.get("version"))
		.append("&agent_id="+map.get("agent_id"))
		.append("&agent_bill_id="+map.get("agent_bill_id"))
		.append("&agent_bill_time="+map.get("agent_bill_time"))
		.append("&pay_type="+map.get("pay_type"))
		.append("&pay_amt="+map.get("pay_amt"))
		.append("&notify_url="+map.get("notify_url"))
		.append("&return_url="+map.get("return_url"))
		.append("&user_ip="+map.get("user_ip"));
		if(map.containsKey("is_test")){
			_sbString.append("&is_test="+map.get("is_test"));
		}
		_sbString.append("&key="+key);
		return Md5Tools.MD5(_sbString.toString()).toLowerCase();
	}
	
	public static String sdkSignMd5(String key,Map<String,String> map){
		StringBuilder _sbString=new StringBuilder();
		_sbString.append("version="+map.get("version"))
		.append("&agent_id="+map.get("agent_id"))
		.append("&agent_bill_id="+map.get("agent_bill_id"))
		.append("&agent_bill_time="+map.get("agent_bill_time"))
		.append("&pay_type="+map.get("pay_type"))
		.append("&pay_amt="+map.get("pay_amt"))
		.append("&notify_url="+map.get("notify_url"))
		.append("&user_ip="+map.get("user_ip"));
		_sbString.append("&key="+key);
		return Md5Tools.MD5(_sbString.toString()).toLowerCase();
	}
	
	public static String qsignMd5(String key,Map<String,String> map){
		StringBuilder _sbString=new StringBuilder();
		_sbString.append("version="+map.get("version"))
		.append("&agent_id="+map.get("agent_id"))
		.append("&agent_bill_id="+map.get("agent_bill_id"))
		.append("&agent_bill_time="+map.get("agent_bill_time"))
		.append("&return_mode="+map.get("return_mode"));
		_sbString.append("&key="+key);
		return Md5Tools.MD5(_sbString.toString()).toLowerCase();
	}
	
	public static boolean checkSign(String key,Map<String,Object> map){
		if(map.get("sign")==null){
			return false;
		}
		//密文
		String sign = map.get("sign").toString();
		
		StringBuilder _sbString=new StringBuilder();
		_sbString.append("result="+map.get("result").toString())
		.append("&agent_id="+map.get("agent_id").toString())
		.append("&jnet_bill_no="+map.get("jnet_bill_no").toString())
		.append("&agent_bill_id="+map.get("agent_bill_id").toString())
		.append("&pay_type="+map.get("pay_type").toString())
		.append("&pay_amt="+map.get("pay_amt").toString())
		.append("&remark="+map.get("remark").toString());
		_sbString.append("&key="+key);
		String md5 = Md5Tools.MD5(_sbString.toString()).toLowerCase();
		return sign.equals(md5);
	}
	
	public static Map<String,String> strToMap(String str){
		Map<String,String> resultMap = new HashMap<>();
		if(str.indexOf("|")==-1){
			return null;
		}
		String arr[]= str.split("\\|");
		for (String string : arr) {
			if(string.indexOf("=")==-1){
				continue;
			}
			String parmArr[] = string.split("=");
			if(parmArr.length==2){
				resultMap.put(parmArr[0], parmArr[1]);
			}
		}
		return resultMap;
	}
	
	/**
     * xml 字符串转 map
     * @param xml
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static String toMap(String xml) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        Document doc = DocumentHelper.parseText(xml);
        Element root = doc.getRootElement();
        //map.put(root.getName(), root.getText());
        if(root.getName().equals("token_id")){
        	return root.getText();
        }
        return "";
    }

	
	//提交地址
	public static String GatewaySubmitUrl(String sign,Map<String,String> map){
		StringBuilder _sbString=new StringBuilder();
		_sbString.append("https://pay.heepay.com/Payment/Index.aspx?");
		
		List<String> keys = new ArrayList<String>(map.keySet());
		//参数值拼接进行加密
        for (int i = 0; i < keys.size(); i++) {
        	String key = keys.get(i);
			String value = map.get(key).toString();
			if(value==null||"".equals(value)){
				continue;
			}
			if(i==0){
				_sbString.append(key + "=" + value);
			}else{
				_sbString.append("&"+key + "=" + value);
			}
		}
		return _sbString.toString();
	}
		
	
}
