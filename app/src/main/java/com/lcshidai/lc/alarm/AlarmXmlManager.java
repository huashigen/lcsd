package com.lcshidai.lc.alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.lcshidai.lc.bdpush.AlarmDialogActivity;

/**
 * 本地闹钟管理类
 * @author 000814
 *
 */
public class AlarmXmlManager {
	
	public static final String ALARM_XML_NAME="xhhconfig_setting";
	public static final String CONTENT="content";
	
	public static void saveXml(String contentstr,Activity content){
		try {
			SharedPreferences user = content.getSharedPreferences(
					ALARM_XML_NAME, 0);
			Editor editor = user.edit();
			editor.putString(CONTENT, contentstr);
			editor.commit();
		} catch (Exception e) {
		}
	}
	
	public static String getContentstr(Activity context){
		String  str="";
		try {
			SharedPreferences user = context.getSharedPreferences(
					ALARM_XML_NAME, 0);
			str = user.getString(CONTENT, "");
		} catch (Exception e) {
			return "";
		}
		return str;	
	}
	
	/**
	 * 
	 * @param contentStr
	 * @return
	 */
	public static ArrayList<String> analyticalStr(String contentStr){
		String[] contentArray=contentStr.split(",");
		ArrayList<String> strArray=new ArrayList<String>();
		for (String string : contentArray) {
			strArray.add(string);
		}
		return strArray;
	}
	
	/**
	 * 删除一个ID
	 * @param id
	 * @param context
	 * @return
	 */
	public static boolean removeId(String id,Activity context){
		try {
			String contentStr = getContentstr(context);
			ArrayList<String> contentArray = analyticalStr(contentStr);
			for (String string : contentArray) {
				if (id.equals(string)) {
					contentArray.remove(string);
				}
			}
			if(contentArray.size()>0){
				String str="";
				for (String string : contentArray) {
					str+=string+",";
				}
				str=str.substring(0, str.length()-1);
				saveXml(str,context);
			}else{
				saveXml("",context);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 添加一个ID
	 * @param id
	 * @param context
	 * @return
	 */
	public static boolean addId(String id,Activity context){
		try {
			String contentStr = getContentstr(context);
			ArrayList<String> contentArray = analyticalStr(contentStr);
			contentArray.add(id);
			if(contentArray.size()>0){
				String str="";
				for (String string : contentArray) {
					str+=string+",";
				}
				str=str.substring(0, str.length()-1);
				saveXml(str,context);
			}else{
				saveXml("",context);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean addAlarm(AlarmInfo aInfo,Activity context){
		try {
			Long time = Long.parseLong(aInfo.remind_time);
			//Long startTime = Long.parseLong(aInfo.start_bid_time);
			 Calendar cal = Calendar.getInstance();
			 Long startTime = Long.parseLong(aInfo.start_bid_time);
			 long nowTime=cal.getTimeInMillis()/1000;
			if (time< nowTime) {
				return false;
			}
			long timeMills=time*1000;
			SimpleDateFormat f=new SimpleDateFormat("HH:mm:ss");
			String nowTimes=f.format(new Date(startTime*1000));
			Intent intent = new Intent(context, AlarmDialogActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("prj_type_name", aInfo.prj_type_name);
			intent.putExtra("now_time", nowTimes);
			intent.putExtra("prj_id", aInfo.obj_id);
			PendingIntent pendingIntent = PendingIntent.getActivity(context,
					Integer.parseInt(aInfo.obj_id), intent, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = (AlarmManager) context
					.getSystemService(Activity.ALARM_SERVICE);
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
				am.setExact(AlarmManager.RTC_WAKEUP, timeMills, pendingIntent);
			} else {
				am.set(AlarmManager.RTC_WAKEUP,timeMills, pendingIntent);
			}
			addId(aInfo.obj_id,context);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean removeAlarm(String id,Activity context){
		try {
			Intent intent = new Intent(context, AlarmDialogActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context,
					Integer.parseInt(id), intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = (AlarmManager) context
					.getSystemService(Activity.ALARM_SERVICE);
			am.cancel(pendingIntent);
			removeId(id, context);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean clearAllAlarm(Activity context){
		try {
			ArrayList<String> idList = analyticalStr(getContentstr(context));
			for (String string : idList) {
				removeAlarm(string, context);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
