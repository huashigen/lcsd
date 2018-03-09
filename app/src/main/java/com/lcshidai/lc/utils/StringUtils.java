package com.lcshidai.lc.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

/**
 *
 */
public class StringUtils {
    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || "null".equals(text);
    }
    
    public static String onHasEmpty(String text) {
         if(isEmpty(text)){
        	 return "0";
         }else{
        	 return text;
         }
    }
    
    /**
     * DecimalFormat转换最简便
     */
    public static String m2(float f) {
        DecimalFormat df = new DecimalFormat("#,000.00");
        if(f<1000){
        	df = new DecimalFormat("#0.00");
        }else if(f>=1000&&f<10000000){
        	df= new DecimalFormat("#0,000.00");
        }else if(f>=10000000){
        	df= new DecimalFormat("#0,000,000.00");
        }
        return df.format(f);
    }
    
    /**
     * DecimalFormat转换最简便
     */
    public static String m2D(Double f) {
        DecimalFormat df = new DecimalFormat("#,000.00");
        if(f<1000){
        	df = new DecimalFormat("#0.00");
        }else if(f>=1000&&f<10000000){
        	df= new DecimalFormat("#0,000.00");
        }else if(f>=10000000){
        	df= new DecimalFormat("#0,000,000.00");
        }
        return df.format(f);
    }
    
    
    /**
     * DecimalFormat转换最简便
     */
    public static String m2Dl(Long f) {
        DecimalFormat df = new DecimalFormat("#,000");
        if(f<1000){
        	df = new DecimalFormat("#0");
        }else if(f>=1000&&f<10000000){
        	df= new DecimalFormat("#0,000");
        }else if(f>=10000000){
        	df= new DecimalFormat("#0,000,000");
        }
        return df.format(f);
    }
    
    
    /**
     * DecimalFormat转换最简便
     */
    public static String m2NF(float f) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(f);
    }
    
    /**
     * DecimalFormat转换最简便
     */
    public static String m2NFS(float f) {
        DecimalFormat df = new DecimalFormat("#0000000000.00");
        return df.format(f);
    }
    
    /**
     * DecimalFormat转换最简便
     */
    public static String m2NFS(Long l) {
        DecimalFormat df = new DecimalFormat("#0000000000");
        return df.format(l);
    }
    
    
    public static String onShowForEmpty(String text){
    	if(isEmpty(text)){
       	    return "-";
        }else{
       	    return text;
        }
    }
    
    public static boolean isFloat(String str){
    	try {
			Float.parseFloat(str);
		} catch (Exception e) {
			return false;
		}
		return true;
    }
    
    public static boolean isInteger(String str){
    	try {
    		Integer.parseInt(str);
		} catch (Exception e) {
			return false;
		}
		return true;
    }
    
    public static boolean isLong(String str){
    	try {
    		Long.parseLong(str);
    		return true;
		} catch (Exception e) {
			return false;
		}
    }
    
    public static boolean isDouble(String str){
    	try {
    		Double.parseDouble(str);
		} catch (Exception e) {
			return false;
		}
		return true;
    }
    
	public static String getFormat(String value){
    	String result = value;
    	if(!isEmpty(value)){
    		//String.format("%.2f", Float.valueOf(value));
    	   BigDecimal bd = new BigDecimal(value);
    	   bd = bd.setScale(0,BigDecimal.ROUND_HALF_UP);  
    	   result = bd.toString();
    	}else{
    	   result = "0";
    	}
    	return result;	
    }
	
	public static String getFormat(String value,int dig){
    	String result = value;
    	if(!isEmpty(value)){
    		//String.format("%.2f", Float.valueOf(value));
    	   BigDecimal bd = new BigDecimal(value);
    	   bd = bd.setScale(dig,BigDecimal.ROUND_HALF_UP);  
    	   result = bd.toString();
    	}else{
    	   result = "0";
    	}
    	return result;	
    }
	
	public static String getFormatF(String value){
    	String result = value;
    	if(!isEmpty(value)){
    		//String.format("%.2f", Float.valueOf(value));
    	   if(value.contains("%")){
    		   result = value.substring(0, value.length()-1);
    	   }
    	}else{
    	   result = "0";
    	}
    	return result;	
    }
	
	/**
	 * 
	 * @param str 该字符串长度（字母和数字算半个长度）
	 * @return
	 */
	public static int getNumAndTRJ(String str){
		int count_TRJ=0,count_num=0,count_oth=0;
		//输入一串数
		char[] chars = str.toCharArray();
		//判断每个字符
		for(int i = 0;i<chars.length;i++){
			if(chars[i]>=97&&chars[i]<=122){//小写字母
				count_TRJ++;
			}else if((chars[i]>=65&&chars[i]<=90)||(chars[i]>=48&&chars[i]<=57)){//大写字母和数字
				count_num++;
			}else{
				count_oth++;
			}
		}
		return (int) Math.rint(count_TRJ/2+count_num/2+count_oth);
	}
	
	/**
	 * 
	 * @param str 改字符串长度（字母和数字算半个长度）
	 * @return
	 */
	public static int getNumAndTRJl(String str,int l){
		int count_TRJ=0,count_num=0,count_oth=0;
		//输入一串数
		char[] chars = str.toCharArray();
		//判断每个字符
		if(l>chars.length)return 0;
		for(int i = 0;i<chars.length;i++){
			if(chars[i]>=97&&chars[i]<=122){
				count_TRJ++;
			}else if((chars[i]>=65&&chars[i]<=90)||(chars[i]>=48&&chars[i]<=57)){
				count_num++;
			}else{
				count_oth++;
			}
			if(((int) Math.rint(count_TRJ/2+count_num/2+count_oth))>=l){
				return i;
			}
		}
		return chars.length;
	}
	
	public static String getKongGeFormat(String value){
		StringBuffer buffer = new StringBuffer();
		String result = value;
		buffer.append(value);
    	int index = 0;
    	char[] tempChar;
		while (index < buffer.length()) {  
			if ((index == 3 || index == 8)) {  
				buffer.insert(index, ' '); 
			}  
			index++;  
		}
		tempChar = new char[buffer.length()];  
		buffer.getChars(0, buffer.length(), tempChar, 0);  
		result = buffer.toString();  
    	return result;	
    }
	
	public static SpannableString assembleString(String str, String value, String color){
    	int len = str.length();
    	String finalStr = str.substring(0, len - 1) + value + str.substring(len - 1);
    	SpannableString ss = new SpannableString(finalStr);
    	ss.setSpan(new ForegroundColorSpan(
    			Color.parseColor(color)), len - 1, finalStr.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    	return ss;
    }
	
//	 public static String getAlpha(String chines) {
//		  String pinyinName = "";
//		  char[] nameChar = chines.toCharArray();
//		  HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//		  defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//		  defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		  for (int i = 0; i < nameChar.length; i++) {
//			   if (nameChar[i] > 128 ) {
//				    try {
//				        pinyinName += PinyinHelper.toHanyuPinyinStringArray(
//				        nameChar[i], defaultFormat)[0].charAt(0);
//				    } catch (BadHanyuPinyinOutputFormatCombination e) {
//				     e.printStackTrace();
//				    }
//			   } else {
//			        pinyinName += nameChar[i];
//			   }
//		  }
//		  return pinyinName;
//	 }
	
	public static String formatLong(long time) {
		StringBuffer str = new StringBuffer();
		long second = time;
		// pi.start_bid_time_diff = second + "";
		long s = second % 60; // 秒
		long mi = (second - s) / 60 % 60; // 分钟
		long h = ((second - s) / 60 - mi) / 60 % 24; // 小时
		long d = (((second - s) / 60 - mi) / 60 - h) / 24; // 天
		if (d > 0) {
//			str.append(d + "天");
			h += d * 24;
		}
		String ss = String.valueOf(s);
		if(s < 10){
			ss = "0" + s;
		}
		String mimi = String.valueOf(mi);
		if(mi < 10){
			mimi = "0" + mi;
		}
		String hh = String.valueOf(h);
		if(h < 10){
			hh = "0" + h;
		}
		str.append(hh + ":");
		str.append(mimi+ ":");
		str.append(ss + "");
		return str.toString();
	}
	/**
	 * unicode 转字符串
	 */
	public static String ox2String(String ox) {
	 
	    StringBuffer string = new StringBuffer();
	 
	    // 转换出每一个代码点
	    int data = Integer.parseInt(ox, 16);
	    
	    // 追加成string
	    string.append((char) data);
	 
	    return string.toString();
	}
	
}
