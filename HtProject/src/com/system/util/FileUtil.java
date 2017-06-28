package com.system.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileUtil
{
	public static ArrayList<String> readFileToList(String filePath,String chartSet)
	{
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;
		ArrayList<String> list = new ArrayList<String>();
		try
		{
			fis = new FileInputStream(filePath);
			isr = new InputStreamReader(fis,chartSet);
			br = new BufferedReader(isr);
			String line = null;  
			while ((line = br.readLine()) != null) 
			{   
				if(!"".equals(line))
					list.add(line);  
			}  
			br.close();  
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			try{if(fis!=null)fis.close();}catch(Exception ex){};
			try{if(isr!=null)isr.close();}catch(Exception ex){};
			try{if(br!=null)br.close();}catch(Exception ex){};
		}
		
		return list;
	}
	
	public static void main(String[] args)
	{
		readFileToList("c:/1.txt","gbk");
	}
}
