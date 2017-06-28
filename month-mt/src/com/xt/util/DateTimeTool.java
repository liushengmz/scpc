package com.xt.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeTool {
	public static String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static String getCurrentMonthFirstday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String s_date = sdf.format(date);
		String s_ym = s_date.substring(0, 7);
		return s_ym + "-01";
	}

	public static int getHourOfDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(11);
	}

	public static int getMinute() {
		Calendar cal = Calendar.getInstance();
		return cal.get(12);
	}

	public static String getNextMonthFirstday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(2, 1);
		cal.set(5, 1);
		return sdf.format(cal.getTime());
	}

	public static String getTomorrow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(5, 1);
		return sdf.format(cal.getTime());
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String getThreeDaysLater() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(5, 3);
		return sdf.format(cal.getTime());
	}
}