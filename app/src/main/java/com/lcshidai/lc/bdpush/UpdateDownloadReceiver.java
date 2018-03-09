package com.lcshidai.lc.bdpush;

import java.io.File;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 点击下载更新包通知的广播
 * @author 000853
 *
 */
public class UpdateDownloadReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		String apkPath = arg1.getStringExtra("apk_path");
		NotificationManager notifiManager = 
			(NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
		notifiManager.cancel("update_down", 1);
		installApk(arg0, apkPath);
		
	}
	
	 /**
     * 安装apk
     * @param url
     */
	private void installApk(Context context, String path){
		File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
	
	}

}
