package com.lcshidai.lc.utils;

import android.util.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

	public static String getTimeStr(Date date) {
		Date today = new Date();
		long interval = today.getTime() - date.getTime();
		today.setHours(23);
		today.setMinutes(59);
		today.setSeconds(59);
		boolean isSameDay = (interval / 86400000) == 0;
		if (isSameDay) {
			if(interval<60*1000){
				return (interval/1000)+"秒前";
			}else if(interval<60*60*1000){
				return (interval/(60*1000))+"分前";
			}else{
				return (interval/(60*60*1000))+"小时前";
			}
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm",
					Locale.ENGLISH);
			return dateFormat.format(date);
		}
	}

	public static String getTimeStr(long date) {
		return getTimeStr(new Date(date));
	}
	
	public static String getCurDate(){
		String date = "";
		Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); //获取当前年份 
        int mMonth = c.get(Calendar.MONTH);//获取当前月份 
        int mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码 
        String sMonth = mMonth+1 < 10 ? "0" + (mMonth+1):String.valueOf(mMonth+1);
	    String sDay = mDay < 10 ? "0" + mDay:String.valueOf(mDay);
        date = mYear + "-" + sMonth + "-" + sDay; 
		return date;
	}

	public static String getCurTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd-hh-mm",
				Locale.ENGLISH);
		Date date=new Date();
		return dateFormat.format(date);
	}

	public static boolean isDateStrToday(String dateStr) {
		Date disDate = strToDateWithFormat(dateStr, "yyyy-MM-dd");
		Date nowDate = new Date();
		String dateStrWithFormat = dateToStrWithFormat(nowDate, "yyyy-MM-dd");
		Date formatNowDate = strToDateWithFormat(dateStrWithFormat, "yyyy-MM-dd");
		int flag = formatNowDate.compareTo(disDate);
		return flag == 0;
	}

	public static Date strToDateWithFormat(String strDate, String formatStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		ParsePosition pos = new ParsePosition(0);
		Date strToDate = null;
		try {
			strToDate = formatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strToDate;
	}

	/**
	 * 将日期转化成指定字符串
	 *
	 * @param date      要转换成字符串的日期
	 * @param formatStr 转换格式
	 * @return 指定格式的日期字符串
	 */
	public static String dateToStrWithFormat(Date date, String formatStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 日期格式转换
	 *
	 * @param dateStr      要转换的字符串
	 * @param sourceFormat 源日期的格式
	 * @param disFormat    目标日期格式
	 * @return 目标日期格式形式的字符串
	 */
	public static String dateShowStyleConvert(String dateStr, String sourceFormat, String disFormat) {
		SimpleDateFormat sourceDateFormat = new SimpleDateFormat(sourceFormat);
		SimpleDateFormat disDateFormat = new SimpleDateFormat(disFormat);
		try {
			return disDateFormat.format(sourceDateFormat.parse(dateStr));
		} catch (Exception e) {
			Log.e(TimeUtil.class.getSimpleName(), "Exception happened!");
			e.printStackTrace();
			return "";
		}
	}
}
