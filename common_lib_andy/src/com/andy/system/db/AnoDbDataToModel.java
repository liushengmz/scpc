package com.andy.system.db;

import java.lang.reflect.Field;
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
			List<AnoFieldClass> list = new ArrayList<AnoFieldClass>();
			
			Class<?> eClass = model.getClassLoader().loadClass(model.getName());

			Field[] fields = eClass.getDeclaredFields();
			
			for (Field field : fields)
			{
				if (field.isAnnotationPresent(DbColumn.class))
				{
					DbColumn column = field.getAnnotation(DbColumn.class);
					field.setAccessible(true);
					String typeName = field.getType().getTypeName();
					AnoFieldClass data = this.new AnoFieldClass();
					data.field = field;
					data.typeName = typeName;
					data.columnName = column.columnName(); 
					list.add(data);
				}
			}
			
			while(rs.next())
			{
				@SuppressWarnings("unchecked")
				T obj = (T)Class.forName(model.getName()).newInstance();
				
				for(AnoFieldClass data : list)
				{
					if (String.class.getName().equals(data.typeName))
					{
						data.field.set(obj, StringUtil.getString(rs.getString(data.columnName),""));
					}
					else if (int.class.getName().equals(data.typeName))
					{
						data.field.set(obj, rs.getInt(data.columnName));
					}
					else if (float.class.getName().equals(data.typeName))
					{
						data.field.set(obj, rs.getFloat(data.columnName));
					}
				}
				
				reList.add(obj);
			}
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		return reList;
	}
	
	private class AnoFieldClass
	{
		String typeName = null;
		String columnName = null;
		Field field = null;
	}
		
	public <T> T fillChildDbModel(Class<? extends BaseDbModel> model,ResultSet rs)
	{
		try
		{
			@SuppressWarnings("unchecked")
			T obj = (T) Class.forName(model.getName()).newInstance();

			Class<?> eClass = model.getClassLoader().loadClass(model.getName());

			Field[] fields = eClass.getDeclaredFields();
			
			for (Field field : fields)
			{
				if (field.isAnnotationPresent(DbColumn.class))
				{
					DbColumn column = field.getAnnotation(DbColumn.class);
					field.setAccessible(true);
					String typeName = field.getType().getTypeName();
					if (String.class.getName().equals(typeName))
					{
						field.set(obj, StringUtil.getString(rs.getString(column.columnName()),""));
					}
					else if (int.class.getName().equals(typeName))
					{
						field.set(obj, rs.getInt(column.columnName()));
					}
					else if (float.class.getName().equals(typeName))
					{
						field.set(obj, rs.getFloat(column.columnName()));
					}
				}
			}

			return obj;
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
		}

		return null;
	}
	
}
