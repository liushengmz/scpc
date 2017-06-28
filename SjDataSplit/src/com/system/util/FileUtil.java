package com.system.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

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
	
	public static boolean saveDataToFile(List<String> list,String path,String fileName)
	{
		BufferedWriter bw = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try
		{
			File basePath = new File(path);
			
			if(!basePath.exists())
				return false;
			
			File writeFile = new File( path + fileName);
			
			if(!writeFile.exists())
				writeFile.createNewFile();
			
			fos = new FileOutputStream(writeFile);
			osw = new OutputStreamWriter(fos,"UTF-8");
			bw = new BufferedWriter(osw);
			
			for(String data : list)
			{
				bw.write(data + "\r\n");
			}
			
			bw.flush();
			
			return true;
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			try{if(bw!=null)bw.close();}catch(Exception ex){};
			try{if(osw!=null)osw.close();}catch(Exception ex){};
			try{if(fos!=null)fos.close();}catch(Exception ex){};
		}
		
		return false;
	}
	
	public static boolean saveDataToFile(StringBuffer data,String path,String fileName)
	{
		BufferedWriter bw = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try
		{
			File basePath = new File(path);
			
			if(!basePath.exists())
				return false;
			
			File writeFile = new File( path + fileName);
			
			if(!writeFile.exists())
				writeFile.createNewFile();
			
			fos = new FileOutputStream(writeFile);
			osw = new OutputStreamWriter(fos,"UTF-8");
			bw = new BufferedWriter(osw);
			
			bw.write(data.toString() + "\r\n");
			
			bw.flush();
			
			return true;
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			try{if(bw!=null)bw.close();}catch(Exception ex){};
			try{if(osw!=null)osw.close();}catch(Exception ex){};
			try{if(fos!=null)fos.close();}catch(Exception ex){};
		}
		
		return false;
	}
	
	public static void main(String[] args)
	{
		readFileToList("c:/1.txt","gbk");
	}
}
