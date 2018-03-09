package com.lcshidai.lc.getui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ZheShangBroadcastReceiver extends BroadcastReceiver {
 
	private String ACTION_INTENT = "com.zsBank.czfinancial.broadcastReceiver";
	private String ACTION_INTENT_RECEIVER = "com.zsBank.czfinancial.receive.broadcastReceiver";
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	try {
    		if(intent != null && intent.getAction().equals(ACTION_INTENT)){
        		String value = intent.getStringExtra("value");
                Intent mIntent = new Intent(ACTION_INTENT_RECEIVER);
                mIntent.putExtra("value", value);  
                context.sendBroadcast(mIntent);
        	} 
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
 
}
