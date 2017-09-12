package com.system.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

import net.sf.json.JSONObject;

public class MapUtil
{
	/**
	 * 主要是用来查看MAP里的值，作DEBUG时使用多
	 * @param map
	 * @return
	 */
	public static <K,V> String trandMapToValue(Map<K,V> map)
	{
		if(map==null || map.size()<=0)
			return null;
		
		StringBuffer sb = new StringBuffer();
		
		int index = 1;
		
		for(K key : map.keySet())
		{
			sb.append(index++ + ":" + key + "-->" + map.get(key) + "\r\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * 检查MAP中是否存在着对应的KEY
	 * @param map
	 * @param keys
	 * @return
	 */
	public static <K,V,M>  boolean isExistKeys(Map<K,V> map, M[] keys)
	{
		if(keys==null || keys.length<=0)
			return false;
		
		for(M key : keys)
		{
			if(map.containsKey(key))
				continue;
			else
				return false;
		}
		
		return true;
	}
	
	/**
	 * 将传入的数据转成指定的JAVA类
	 * @param map key的值一定全部是小写，否则可能因为大小写的问题获取不到正确的属性值
	 * @param beanClass
	 * @return
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass)
	{      
        if (map == null)     
            return null;      
     
		try
		{
			Object obj = beanClass.newInstance();

			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			java.beans.PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (java.beans.PropertyDescriptor property : propertyDescriptors)
			{
				Method setter = property.getWriteMethod();
				if (setter != null)
				{
					System.out.println(property.getName());
					//全部转成小写，统一使用
					setter.invoke(obj, map.get(property.getName().toLowerCase()));
				}
			}
			return obj;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
    }      
	
	public static void main(String[] args)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Name", "Andy.Chen");
		map.put("Age", 27);
		map.put("ADDRESS", "在那遥远的小地方");
		List<Map<String,Object> > list = new ArrayList<Map<String,Object>>();
		map.put("Children", list);
		for(int i=0; i<5; i++)
		{
			Map<String, Object> child = new HashMap<String, Object>();
			child.put("Sex", "GIRL" + i);
			child.put("Name", "LISAI" + i);
			child.put("Age", 9 + i);
			list.add(child);
		}
		System.out.println(StringUtil.getJsonFormObject(map));
		
	}
	
}
