
package com.andy.system.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateFormatUtil
{
	private static Logger logger = Logger.getLogger(DateFormatUtil.class);
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_TIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyyMMddHHmmss
	 */
	public static final String DATE_TIME_FORMAT_2 = "yyyyMMddHHmmss";
	/**
	 *yyyy-MM-dd-HH-mm-ss 
	 */
	public static final String DATE_TIME_FORMAT_3 = "yyyy-MM-dd-HH-mm-ss";
	/**
	 * yyyy-MM-dd
	 */
	public static final String DATE_TIME_FORMAT_4 = "yyyy-MM-dd";
	/**
	 * yyyyMM
	 */
	public static final String DATE_TIME_FORMAT_5 = "yyyyMM";
	/**
	 * yyyyMMdd
	 */
	public static final String DATE_TIME_FORMAT_6 = "yyyyMMdd";
	/**
	 * HH:mm:ss
	 */
	public static final String DATE_TIME_FORMAT_7 = "HH:mm:ss";
	/**
	 * HHmmss
	 */
	public static final String DATE_TIME_FORMAT_8 = "HHmmss";
	/**
	 * HHmm
	 */
	public static final String DATE_TIME_FORMAT_9 = "HHmm";
	
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT_4);

	/**
	 * 
	 * @param date 日期
	 * @param format 格式 
	 * @return 出错的情况下返回  当前时间 yyyy-MM-dd 的格式数据
	 */
	public static String dateToStr(Date date, String format)
	{
		if (date == null)
			return null;
		
		try
		{
			sdf.applyPattern(format);
			
			return sdf.format(date);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());	
		}
		
		sdf.format(DATE_TIME_FORMAT_4);
		
		return sdf.format(new Date());
	}
	
	/**
	 * 获取当前月份的最后一天，并以指定的格式返回
	 * @param format
	 * @return
	 */
	public static String getCurMonthEndDate(String format)
	{
		Calendar ca = Calendar.getInstance();
		
		ca.set(Calendar.DAY_OF_MONTH, 1);
		
		ca.add(Calendar.MONTH, 1);
		
		ca.add(Calendar.DAY_OF_YEAR, -1);
		
		return dateToStr(ca.getTime(),format);
	}
	
	public static void main(String[] args)
	{
		//System.out.println(dateToStr(new Date(), DateFormatUtil.DATE_TIME_FORMAT_5));
		System.out.println(getCurMonthEndDate("dd"));
	}
}
