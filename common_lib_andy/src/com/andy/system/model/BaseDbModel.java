package com.andy.system.model;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class BaseDbModel
{
	private Logger logger = Logger.getLogger(BaseDbModel.class);
	
	@Override
	public String toString()
	{
		try
		{
			Field[] fs = this.getClass().getDeclaredFields();
			
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			
			for(Field f : fs)
			{
				f.setAccessible(true);
				map.put(f.getName(), f.get(this));
			}
			
			return map.toString();
		}
		catch(Exception ex){ logger.error(ex.getMessage()); }
		
		return super.toString();
	}
}
