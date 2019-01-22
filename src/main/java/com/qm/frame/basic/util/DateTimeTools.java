package com.qm.frame.basic.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright © 2018浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2018年11月24日 上午1:57:42
 * @Description 时间日期工具
 */
public class DateTimeTools {

	/**
	 * @Title getFormatDate
	 * @param aDate
	 * @param aFormat
	 * @return
	 * @Description 获取指定日期的字符串
	 */
	public static String getFormatDate(Date aDate, String aFormat) {

		SimpleDateFormat tDateF = null;
		if (aFormat == null) {
			tDateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if ("".equals(aFormat)) {
			tDateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else {
			tDateF = new SimpleDateFormat(aFormat);
		}

		return tDateF.format(aDate);
	}

	/**
	 * @Title compareDate
	 * @param dt1
	 * @param dt2
	 * @return
	 * @Description 按照年月日比较两个 日期时间的大小
	 */
	public static int compareDate(Date dt1, Date dt2) {

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dt1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dt2);

		int y1 = cal1.get(Calendar.YEAR);
		int m1 = cal1.get(Calendar.MONTH);
		int d1 = cal1.get(Calendar.DAY_OF_MONTH);

		int y2 = cal2.get(Calendar.YEAR);
		int m2 = cal2.get(Calendar.MONTH);
		int d2 = cal2.get(Calendar.DAY_OF_MONTH);

		if (y1 > y2) {
			return 1;

		} else if (y1 == y2) {
			if (m1 > m2) {
				return 1;

			} else if (m1 == m2) {
				if (d1 > d2) {
					return 1;
				} else if (d1 == d2) {

					return 0;
				} else {
					return -1;

				}

			} else {
				return -1;
			}

		} else {
			return -1;
		}

	}
	
	
    @Deprecated
	public static Date parse(String string) {
        try {
            DateFormat format = DateFormat.getDateInstance();
            return format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parse2(String string) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Title getTodayTime
     * @return
     * @Description 获取当前日期时间
     */
    public static String getTodayTime() {
        Date sellTime = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(sellTime);
        System.out.println(cal.toString());
        DateFormat format = DateFormat.getDateInstance();

        System.out.println(format.format(sellTime));
        return null;
    }

    /**
     * @Title getTodayDate
     * @return
     * @Description 获取当天日期(短日期)
     */
    public static String getTodayDate() {
        Date sellTime = new Date();
        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
        return dateformat1.format(sellTime);
    }

    public static String getMonth_day(Date date, String str) {
        if (str == null)
            str = "/";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return (cal.get(Calendar.MONTH) + 1) + str + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @Title getTodayDateLong
     * @return
     * @Description 获取当天日期(长日期)
     */
    public static String getTodayDateLong() {
        Date sellTime = new Date();
        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateformat1.format(sellTime);
    }

    /**
     * @Title currentYear
     * @return
     * @Description 获取当前年份
     */
    public static int currentYear() {

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        // int month = cal.get(Calendar.MONTH )+1;

        // System.out.println(year + " 年 " + month + " 月");

        return year;
    }

    /**
     * @Title format
     * @param date
     * @param arg
     * @return
     * @Description 将日期格式化
     */
    public static String format(Date date, String arg) {
        if (date == null)
            return null;
        if (arg == null)
            arg = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(arg);
        return sdf.format(date);
    }

    public static Date stringToDate(String str) throws ParseException {
        if (str.split("-").length == 2) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            return sdf.parse(str);
        } else if(str.split("-").length == 1){ 
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            return sdf.parse(str);
        } else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(str);
        }

    }

    /**
     * @Title countDays
     * @param begin
     * @param end
     * @return
     * @Description 计算俩个日期之间有多少天
     */
    public static int countDays(String begin,String end){
          int days = 0;
          
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
          Calendar c_b = Calendar.getInstance();
          Calendar c_e = Calendar.getInstance();
          
          try{
           c_b.setTime(df.parse(begin));
           c_e.setTime(df.parse(end));
           
           while(c_b.before(c_e)){
            days++;
            c_b.add(Calendar.DAY_OF_YEAR, 1);
           }
           
          }catch(ParseException pe){
           System.out.println("日期格式必须为：yyyy-MM-dd；如：2010-4-4.");
          }
          
          return days; 
        } 
    
    /**
     * @Title addCalendarDay
     * @param calDate
     * @param addDate
     * @return
     * @Description 计算当前距离当前日期之后的某个日期
     */
    public static String addCalendarDay(Date calDate, long addDate) {
        long time = calDate.getTime();
        addDate = addDate * 24 * 60 * 60 * 1000;
        time += addDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(time));
    }
    
    public static Date addDay(Date calDate, long addDate) {
        long time = calDate.getTime();
        addDate = addDate * 24 * 60 * 60 * 1000;
        time += addDate;
        return new Date(time);
    }
    
    /**
     * @Title reduceDay
     * @param calDate
     * @param addDate
     * @return
     * @Description 计算距离执行日期提前多少天的日期
     */
    public static Date reduceDay(Date calDate, long addDate) {
        long time = calDate.getTime();
        addDate = addDate * 24 * 60 * 60 * 1000;
        time -= addDate;
        return new Date(time);
    }
    
    public static void main(String[] args) {
		Date date = parse("2009.10-13");
		System.out.println(date);
	}

}
