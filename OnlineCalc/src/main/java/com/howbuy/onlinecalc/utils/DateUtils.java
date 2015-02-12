package com.howbuy.onlinecalc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static final String PATTERN = "yyyyMMdd";

	/**
	 * 日期前一天
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static String getNextDate(Date date, int field, int size) {
		SimpleDateFormat df = new SimpleDateFormat(PATTERN);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, size);
		return df.format(calendar.getTime());
	}
	
	/**
	 * 日期后一天
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static String getBeforeDate(Date date, int field, int size) {
		SimpleDateFormat df = new SimpleDateFormat(PATTERN);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, -size);
		return df.format(calendar.getTime());
	}

	/**
	 * 计算两天之间的天数.
	 */
	public static int daysBetween(String start, String end) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(parserDate(start));
			long time1 = cal.getTimeInMillis();
			cal.setTime(parserDate(end));
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getDateString(Date date) {
		return new SimpleDateFormat(PATTERN).format(date == null ? new Date() : date);
	}

	public static Date parserDate(String datestr) throws Exception {
		return new SimpleDateFormat(PATTERN).parse(datestr);
	}

	public static void main(String[] args) throws Exception{
		//System.out.println(daysBetween("20141125","20141125"));
		  String jzrq = DateUtils.getBeforeDate(DateUtils.parserDate("20141125"), Calendar.DAY_OF_MONTH, 60);
		  System.out.println(jzrq);
	}
}
