package com.lcshidai.lc.utils;

import com.lcshidai.lc.utils.onekeyshare.OnekeyShare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;

/**
 * 一键分享工具类
 * @author 000853
 *
 */
public class OneKeyShareUtil {
	
	private Context mContext;

	public OneKeyShareUtil(Context context){
		this.mContext = context;
	}
	
	
	public void unInitShare(){
		ShareSDK.stopSDK(mContext);
	}
	
	/**
	 * 删除授权（腾讯微博、新浪微博）
	 */
	public void removeAuthAccount() throws Exception {
		Platform tencentWeibo = ShareSDK.getPlatform(mContext, TencentWeibo.NAME);
		if(tencentWeibo.isValid()){
			tencentWeibo.removeAccount();
		}
		Platform sinaWeibo = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
		if(sinaWeibo.isValid()){
			sinaWeibo.removeAccount();
		}
	}
	
	
	public void toShare(String msg, PlatformActionListener listener){
		
		String message = msg.replaceAll(" ", "");

		ShareSDK.initSDK(mContext);

		//判断是否清除保存的账号信息
		SharedPreferences user = mContext.getSharedPreferences(
				"config_setting", Context.MODE_PRIVATE);
		String last_UID = user.getString("last_UID", "");
		String UID = user.getString("UID", "");
		if(!last_UID.equals(UID)){
			try {
				removeAuthAccount();
			} catch (Exception e) {
				e.printStackTrace();
			}
			user.edit().putString("last_UID", UID).commit();
		}
		
		
		OnekeyShare oks = new OnekeyShare();
		
		// 分享时Notification的图标和文字
//		oks.setNotification(R.drawable.ic_launcher, getContext().getString(R.string.app_name));
		// address是接收人地址，仅在信息和邮件使用
		oks.setAddress("");
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		oks.setTitle("Title");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(message);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath(MainActivity.TEST_IMAGE);
		// imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、 微信、易信、Linked-In支持此字段
//		oks.setImageUrl("http://sharesdk.cn/ rest.png");
		//微信、易信中使用，表示视屏地址或网页地址
//		oks.setUrl("http://sharesdk.cn");
		// appPath是待分享应用程序的本地路径，仅在微信中使用
//		oks.setAppPath(MainActivity.TEST_IMAGE);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		oks.setComment(getContext().getString(R.string.share));
		// site是分享此内容的网站名称，仅在QQ空间使用
//		oks.setSite(context.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl("http://sharesdk.cn");
		// venueName是分享社区名称，仅在Foursquare使用
//		oks.setVenueName("Southeast in China");
		// venueDescription是分享社区描述，仅在Foursquare使用
//		oks.setVenueDescription("This is a beautiful place!");
		// latitude是纬度数据，仅在新浪微博、腾讯微博和Foursquare使用
//		oks.setLatitude(23.122619f);
		// longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
//		oks.setLongitude(113.372338f);
		// 是否直接分享（true则直接分享）
		oks.setSilent(false);
		// 指定分享平台，和slient一起使用可以直接分享到指定的平台
//		if (platform != null) { 
//			oks.setPlatform(platform); 
//		}
		// 可通过OneKeyShareCallback来捕获快捷分享的处理结果 
		oks.setCallback(listener);
		
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 为EditPage设置一个背景的View
		View v = ((ViewGroup)((Activity)mContext).findViewById(android.R.id.content)).getChildAt(0);
		oks.setEditPageBackground(v);
		
		oks.show(mContext);
	}
	
	public void toShare(String msg, String title, String titleUrl, String imgPath, PlatformActionListener listener){
		String message = msg.replaceAll(" ", "");

		ShareSDK.initSDK(mContext);

		//判断是否清除保存的账号信息
		SharedPreferences user = mContext.getSharedPreferences(
				"config_setting", Context.MODE_PRIVATE);
		String last_UID = user.getString("last_UID", "");
		String UID = user.getString("UID", "");
		if(!last_UID.equals(UID)){
			try {
				removeAuthAccount();
			} catch (Exception e) {
				e.printStackTrace();
			}
			user.edit().putString("last_UID", UID).commit();
		}
		
		
		OnekeyShare oks = new OnekeyShare();
		
		// address是接收人地址，仅在信息和邮件使用
		oks.setAddress("");
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(titleUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(message);
		//微信分享的url
		oks.setUrl(titleUrl);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(imgPath);
		// 是否直接分享（true则直接分享）
		oks.setSilent(false);
		// 可通过OneKeyShareCallback来捕获快捷分享的处理结果 
		oks.setCallback(listener);
		
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 为EditPage设置一个背景的View
		View v = ((ViewGroup)((Activity)mContext).findViewById(android.R.id.content)).getChildAt(0);
		oks.setEditPageBackground(v);
		
		oks.show(mContext);
	}
	
	public void toShareForImageUrl(String msg, String title, String titleUrl, String imageUrl, PlatformActionListener listener){
		String message = msg.replaceAll(" ", "");

		ShareSDK.initSDK(mContext);

		//判断是否清除保存的账号信息
		SharedPreferences user = mContext.getSharedPreferences(
				"config_setting", Context.MODE_PRIVATE);
		String last_UID = user.getString("last_UID", "");
		String UID = user.getString("UID", "");
		if(!last_UID.equals(UID)){
			try {
				removeAuthAccount();
			} catch (Exception e) {
				e.printStackTrace();
			}
			user.edit().putString("last_UID", UID).commit();
		}
		
		
		OnekeyShare oks = new OnekeyShare();
		
		// address是接收人地址，仅在信息和邮件使用
		oks.setAddress("");
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(titleUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(message);
		//微信分享的url
		oks.setUrl(titleUrl);
		
		oks.setImageUrl(imageUrl);
		// 是否直接分享（true则直接分享）
		oks.setSilent(false);
		// 可通过OneKeyShareCallback来捕获快捷分享的处理结果 
		oks.setCallback(listener);
		
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 为EditPage设置一个背景的View
		View v = ((ViewGroup)((Activity)mContext).findViewById(android.R.id.content)).getChildAt(0);
		oks.setEditPageBackground(v);
		
		oks.show(mContext);
	}
	
	public void toShareForImageUrlMSG(String msg,String desn,  String title, String titleUrl, String imageUrl, PlatformActionListener listener){
		String message = msg.replaceAll(" ", "");

		ShareSDK.initSDK(mContext);

		//判断是否清除保存的账号信息
		SharedPreferences user = mContext.getSharedPreferences(
				"config_setting", Context.MODE_PRIVATE);
		String last_UID = user.getString("last_UID", "");
		String UID = user.getString("UID", "");
		if(!StringUtils.isEmpty(last_UID)&&!StringUtils.isEmpty(UID)&&!last_UID.equals(UID)){
			try {
				removeAuthAccount();
			} catch (Exception e) {
				e.printStackTrace();
			}
			user.edit().putString("last_UID", UID).commit();
		}
		
		
		OnekeyShare oks = new OnekeyShare();
		
		// address是接收人地址，仅在信息和邮件使用
		oks.setAddress("");
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title);
		oks.setDesn(desn);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(titleUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(message);
		//微信分享的url
		oks.setUrl(titleUrl);
		
		oks.setImageUrl(imageUrl);
		// 是否直接分享（true则直接分享）
		oks.setSilent(false);
		// 可通过OneKeyShareCallback来捕获快捷分享的处理结果 
		oks.setCallback(listener);
		
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 为EditPage设置一个背景的View
		View v = ((ViewGroup)((Activity)mContext).findViewById(android.R.id.content)).getChildAt(0);
		oks.setEditPageBackground(v);
		
		oks.show(mContext);
	}
	
}
