
package com.system.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;


public class SerializeUtil
{
	private static Logger log = Logger.getLogger(SerializeUtil.class);
	
	public static byte[] serialize(Object object)
	{
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try
		{
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	public static Object unserialize(byte[] bytes)
	{
		ByteArrayInputStream bais = null;
		try
		{
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		String value = "中国";
		
		String[] kos = {"GBK","GB2312","UTF-8","ISO-8859-1"};
		
		for(int i=0; i<kos.length; i++)
		{
			for(int j=0; j<kos.length; j++)
			{
				if(i!=j)
				{
					try
					{
						System.out.println(kos[i] + "-->" + kos[j] + "-->" + new String(value.getBytes(kos[i]),kos[j]));
					}
					catch (UnsupportedEncodingException e)
					{
						
						e.printStackTrace();
					}
				}
			}
		}
		
	}
}
