package com.system.util;

public class SqlUtil {

	public static String sqlEncode(String txt) {
		if (StringUtil.isNullOrEmpty(txt))
			return "";
		return txt.replace("\\", "\\\\").replace("\'", "\\\'");
	}

	
	public static void main(String[] args)
	{
		System.out.println(Base64UTF.decode("5oiQ5Yqf"));
	}
}
