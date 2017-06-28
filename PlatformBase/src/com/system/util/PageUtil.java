package com.system.util;

import java.util.Map;

import com.system.constant.Constant;

public class PageUtil
{
	public static String initPageQuery(String url,Map<String, String> params,int rowCount,int pageIndex)
	{
		if(rowCount==0)
			return "";
		
		int pageCount = (rowCount + Constant.PAGE_SIZE -1)/Constant.PAGE_SIZE;
		
		String result =  "共&nbsp;" + rowCount + "&nbsp;条记录 &nbsp;" + pageIndex + "/" + pageCount + "&nbsp;";
		
		String prePage = "";
		String nextPage = "";
		
		String query = url + "?";
		
		if(params!=null)
		{
			for(String key : params.keySet())
			{
				query += key + "=" + params.get(key) + "&";
			}
		}
		
		if(query.length()>1 && params!=null)
			query = query.substring(0, query.length()-1);
		
		if(pageIndex!=1)
			prePage = "<a href=\""+ query +"&pageindex=" + (pageIndex-1) + "\">上一页</a>&nbsp;&nbsp;";
		else
			prePage = "上一页&nbsp;";
		
		result += prePage;
		
		if(pageIndex!=pageCount)
			nextPage = "<a href=\""+ query +"&pageindex=" + (pageIndex+1) + "\">下一页</a>&nbsp;&nbsp;";
		else
			nextPage = "下一页&nbsp;";
		
		result += nextPage;
		
		if(1!=pageCount)
		{
			
			for(int i=1; i<=pageCount; i++)
			{
				if(i!=pageIndex)
				{
					if(i>10)
					{
						if(i%5==0 || (i+5)>pageCount  || ((i>(pageIndex-5) && i< (pageIndex+5))))
							result += "<a href=\""+ query +"&pageindex=" + (i) + "\">" + i + "</a>&nbsp;&nbsp;";
					}
					else
						result += "<a href=\""+ query +"&pageindex=" + (i) + "\">" + i + "</a>&nbsp;&nbsp;";
				}
				else
				{
					result += pageIndex + "&nbsp;&nbsp;";
				}
			}
		}
		
		return result;
	}
	
	public static String queryFilter(String query,String... strings)
	{
		if(StringUtil.isNullOrEmpty(query)||"null".equalsIgnoreCase(query))
			return "";
		
		 for (int i = 0; i < strings.length; i++)
		 {
			 query = query.replaceAll("(^|&)("+ strings[i] +")=.{0,}?($|&)","");
		 }
		
		return query;
	}
	
}
