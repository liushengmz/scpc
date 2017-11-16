package com.andy.system.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.andy.system.util.StringUtil;

public class SignUtil
{
	/**
	 * 将params里面的KEYS进行排序，然后根据排好序后的KEYS进行取对应的值，进行相连，用逗号隔开，最后将加密的KEY连在后面，形成MD5的原始加密字符串
	 * @param params
	 * @param key
	 * @return
	 */
	public static String getSign1(Map<String, String> params,String key)
	{
		Object[] keys = params.keySet().toArray();
		
		Arrays.sort(keys);
		
		String sourceKey = "";
			
		for(Object s : keys)
		{
			if(!"sign".equals(s))
				sourceKey += params.get(s) + ",";
		}
		
		sourceKey += key;
		
		return StringUtil.getMd5String(sourceKey, 32);
	}
	
	public static void main(String[] args)
	{
		Map<String, String> params = new HashMap<>();
		params.put("name", "andy");
		params.put("price", "1234");
		params.put("age", "18");
		params.put("address", "yijials");
		params.put("order", "sells");
		params.put("level", "1");
		
		System.out.println(getSign1(params, "dfwqweqrewrqew"));
	}
}
