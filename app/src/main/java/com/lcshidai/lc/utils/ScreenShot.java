package com.lcshidai.lc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

public class ScreenShot {
	/**
	 * 截图
	 * @param activity
	 * @param containsTitlebar
	 * @return
	 */
	public static Bitmap takeShot(Activity activity, boolean containsTitlebar) {
		// 截取activity对应的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        
        bitmap = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
		return bitmap;
	}
	
	/**
	 * 指定高度缩放图片
	 * @param photo
	 * @param newHeight
	 * @param context
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap photo, int newHeight, Context context) {
		final float densityMultiplier = context.getResources().getDisplayMetrics().density;
		int h = (int) (newHeight * densityMultiplier);
		int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

		Bitmap nb = Bitmap.createScaledBitmap(photo, w, h, true);
		photo.recycle();
		return nb;
	}
	
	public static Bitmap scaleBitmap(Bitmap origin, double scale) {
		scale = 0.5;
		int w = (int) (origin.getWidth() * scale);
		int h = (int) (origin.getHeight() * scale);
		Bitmap nb = Bitmap.createScaledBitmap(origin, w, h, true);
		origin.recycle();
		return nb;
	}

	/**
	 * 保存图片到指定路径
	 * @param bitmap
	 * @param dir
	 * @param filename
	 * @return
	 */
	public static boolean savePicture(Bitmap bitmap, File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			if (null != fos) {
			    if(file.getName().endsWith("jpg")){
				    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			    }else if(file.getName().endsWith("png")){
			    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			    }
				fos.flush();
				fos.close();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
