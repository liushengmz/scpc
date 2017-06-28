package com.system.util;

import java.util.HashMap;
import java.util.Map;

import com.system.constant.Constant;

public class PageUtil {
	public static String initPageQuery2(String url, Map<String, String> params, int rowCount, int pageIndex) {
		if (rowCount == 0)
			return "";

		int pageCount = (rowCount + Constant.PAGE_SIZE - 1) / Constant.PAGE_SIZE;

		String result = "共&nbsp;" + rowCount + "&nbsp;条记录 &nbsp;" + pageIndex + "/" + pageCount + "&nbsp;";

		String prePage = "";
		String nextPage = "";

		String query = url + "?";

		if (params != null) {
			for (String key : params.keySet()) {
				query += key + "=" + params.get(key) + "&";
			}
		}

		if (query.length() > 1 && params != null)
			query = query.substring(0, query.length() - 1);

		if (pageIndex != 1)
			prePage = "<a href=\"" + query + "&pageindex=" + (pageIndex - 1) + "\">上一页</a>&nbsp;&nbsp;";
		else
			prePage = "上一页&nbsp;";

		result += prePage;

		if (pageIndex != pageCount)
			nextPage = "<a href=\"" + query + "&pageindex=" + (pageIndex + 1) + "\">下一页</a>&nbsp;&nbsp;";
		else
			nextPage = "下一页&nbsp;";

		result += nextPage;

		if (1 != pageCount) {

			for (int i = 1; i <= pageCount; i++) {
				if (i != pageIndex) {
					if (i > 10) {
						if (i % 5 == 0 || (i + 5) > pageCount || ((i > (pageIndex - 5) && i < (pageIndex + 5))))
							result += "<a href=\"" + query + "&pageindex=" + (i) + "\">" + i + "</a>&nbsp;&nbsp;";
					} else
						result += "<a href=\"" + query + "&pageindex=" + (i) + "\">" + i + "</a>&nbsp;&nbsp;";
				} else {
					result += pageIndex + "&nbsp;&nbsp;";
				}
			}
		}

		return result;
	}
	
	public static String initPageQuery(String url, Map<String, String> params,
			int rowCount, int pageIndex)
	{
		if (rowCount == 0)
			return "";

		int pageCount = (rowCount + Constant.PAGE_SIZE - 1)
				/ Constant.PAGE_SIZE;

		String result = "<dd class='dd04_me'>共&nbsp;" + rowCount + "&nbsp;条记录 &nbsp;" + pageIndex
				+ "/" + pageCount + "&nbsp;";

		String prePage = "";
		String nextPage = "";
		
		String pageIndexHead = "&pageindex=";

		String query = url + "?";

		if (params != null)
		{
			for (String key : params.keySet())
			{
				query += key + "=" + params.get(key) + "&";
			}
		}
		else
			pageIndexHead = "pageindex=";

		if (query.length() > 1 && params != null)
			query = query.substring(0, query.length() - 1);

		if (pageIndex != 1)
			prePage = "<a href=\"" + query + pageIndexHead + (pageIndex - 1)
					+ "\">上一页</a>&nbsp;&nbsp;";
		else
			prePage = "上一页&nbsp;";

		result += prePage;

		if (pageIndex != pageCount)
			nextPage = "<a href=\"" + query + pageIndexHead + (pageIndex + 1)
					+ "\">下一页</a>&nbsp;&nbsp;";
		else
			nextPage = "下一页&nbsp;";

		result += nextPage;

		result += "<script> function pageJump() { var pageIndex = document.getElementById('sel_page').selectedIndex; if(pageIndex>=0){ window.location.href = '" + query + pageIndexHead + "' + (pageIndex + 1); } } </script><select id='sel_page'>";
		
		for (int i = 1; i <= pageCount; i++)
		{
			result += "<option " + (pageIndex==i ? "selected='selected'" : "") + " value='" + i + "'>" + i + "</option>";
		}

		return result + "</select>&nbsp;&nbsp;<input type='button' value='跳&nbsp;转' onclick='pageJump()' /></dd>";
	}
	

	public static String queryFilter(String query, String... strings) {
		if (StringUtil.isNullOrEmpty(query) || "null".equalsIgnoreCase(query))
			return "";

		for (int i = 0; i < strings.length; i++) {
			query = query.replaceAll("(^|&)(" + strings[i] + ")=.{0,}?($|&)", "");
		}

		return query;
	}

	public static String htmlEncode(int i) {

		if (i == '&')
			return "&amp;";

		else if (i == '<')
			return "&lt;";

		else if (i == '>')
			return "&gt;";

		else if (i == '"')
			return "&quot;";

		else
			return "" + (char) i;

	}

	public static String htmlEncode(String st) {

		StringBuffer buf = new StringBuffer();
		
		for (int i = 0; i < st.length(); i++) {

			buf.append(htmlEncode(st.charAt(i)));

		}

		return buf.toString();

	}

	
	public static void main(String[] args)
	{
		Map<String,String> map = new HashMap<String, String>();
		map.put("query", "aldsfla");
		map.put("data", "fdsfds");
		String data = initPageQuery2("http://www.baidu.com/", map, 340, 5);
		System.out.println(data);
	}
	
	
}
