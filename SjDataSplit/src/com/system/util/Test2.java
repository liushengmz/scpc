package com.system.util;

import java.util.Calendar;

public class Test2
{
	public static void main(String[] args)
	{
		String sql = "INSERT INTO tbl_wm_trone_order_year_month(trone_order_id,YEAR,MONTH) VALUES \r\n";
		StringBuffer sb = new StringBuffer(250);
		for(int i=1;i<=29; i++)
		{
			Calendar ca = Calendar.getInstance();
			ca.set(2015, 11, 1);
			for(int j=1;j<=24;j++)
			{
				ca.add(Calendar.MONTH, 1);
				sb.append("(" + i + "," + ca.get(Calendar.YEAR) + "," + (ca.get(Calendar.MONTH) + 1) + "),\r\n");
			}
		}
		System.out.println(sql + sb.substring(0,sb.length()-3) + ";");
		
	}
}
