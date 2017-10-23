package com.andy.system.util;

import java.text.DecimalFormat;

public class DigitUtil
{
	/**
	 * ###0.00
	 */
	public static final String DECIMAL_FORMAT_1 = "###0.00";
	/**
	 * ###0.00%
	 */
	public static final String DECIMAL_FORMAT_2 = "###0.00%";
	
	private static DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT_1);
	
	public static String getPercent(double data1,double data2,String format)
	{
		data2 = data2==0 ? 1 : data2;
		df.applyPattern(format);
		return df.format(data1/data2);
	}
	
	public static String dataToStr(double data, String format)
	{
		df.applyPattern(format);
		return df.format(data);
	}
	
	public static void main(String[] args)
	{
		System.out.println(dataToStr(123,DECIMAL_FORMAT_1));
		System.out.println(getPercent(123.343, 188.28,DECIMAL_FORMAT_2));
	}
	
}
