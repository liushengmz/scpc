package com.andy.system.db;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.andy.system.annotation.DbColumn;
import com.andy.system.model.BaseDbModel;
import com.andy.system.util.StringUtil;

public class AnoDbDataToModel
{
	private static Logger logger = Logger.getLogger(AnoDbDataToModel.class);
	
	public <T> List<T> fillChildDbModelList(Class<? extends BaseDbModel> model,ResultSet rs)
	{
		List<T> reList = new ArrayList<T>();
		
		try
		{
			List<AnoMethodClass> list = new ArrayList<AnoMethodClass>();
			
			for(Method method : model.getClassLoader().loadClass(model.getName()).getMethods())
			{
				//如果当前方法有注解为DbColumn
				if (method.isAnnotationPresent(DbColumn.class))
				{
					Type[] types = method.getGenericParameterTypes();
					
					String typeName = types[0].getTypeName();
					
					DbColumn column = method.getAnnotation(DbColumn.class);
					
					AnoMethodClass data = this.new AnoMethodClass();
					
					data.method = method;
					data.typeName = typeName;
					data.columnName = column.columnName();
					
					if(StringUtil.isNullOrEmpty(data.columnName))
					{
						logger.info(method.getName() + " DbColumn empty");
						continue;
					}
					
					list.add(data);
				}
			}
			
			while(rs.next())
			{
				@SuppressWarnings("unchecked")
				T obj = (T)Class.forName(model.getName()).newInstance();
				
				reList.add(obj);
				
				for(AnoMethodClass data : list)
				{
					if(String.class.getName().equals(data.typeName))
					{
						data.method.invoke(obj, StringUtil.getString(rs.getString(data.columnName),""));
					}
					else if(int.class.getName().equals(data.typeName))
					{
						data.method.invoke(obj, rs.getInt(data.columnName));
					}
					else if(float.class.getName().equals(data.typeName))
					{
						data.method.invoke(obj, rs.getFloat(data.columnName));
					}
					else if(float.class.getName().equals(data.typeName))
					{
						data.method.invoke(obj, rs.getDouble(data.columnName));
					}
				}
			}
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		return reList;
	}
	
	private class AnoMethodClass
	{
		Method method = null;
		String typeName = null;
		String columnName = null;
	}
	
	public <T> T fillChildDbModel(Class<? extends BaseDbModel> model,ResultSet rs)
	{
		try
		{
			@SuppressWarnings("unchecked")
			T obj = (T)Class.forName(model.getName()).newInstance();
			
			for(Method method : model.getClassLoader().loadClass(model.getName()).getMethods())
			{
				//如果当前方法有注解为DbColumn
				if (method.isAnnotationPresent(DbColumn.class))
				{
					Type[] types = method.getGenericParameterTypes();
					
					String typeName = types[0].getTypeName();
					
					DbColumn column = method.getAnnotation(DbColumn.class);
					
					if(String.class.getName().equals(typeName))
					{
						method.invoke(obj, rs.getString(column.columnName()));
					}
					else if(int.class.getName().equals(typeName))
					{
						method.invoke(obj, rs.getInt(column.columnName()));
					}
					else if(float.class.getName().equals(typeName))
					{
						method.invoke(obj, rs.getFloat(column.columnName()));
					}
					else if(float.class.getName().equals(typeName))
					{
						method.invoke(obj, rs.getDouble(column.columnName()));
					}
				}
			}
			
			return obj;
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		return null;
	}
}
