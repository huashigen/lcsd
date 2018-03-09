package com.lcshidai.lc.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.EditText;

import com.lcshidai.lc.widget.text.CustomEditTextDel;
import com.lcshidai.lc.widget.text.CustomEditTextLeftIcon;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 短信验证码自动填充类
 */
public class SmsAutoUtil {

    private static final String SMS_URL_INBOX = "content://sms/inbox";
    //	private static SmsAutoUtil sau;
    // 	private SmsContent smsCon;
    private MyLoaderCallback mCallback;

    private SmsAutoUtil() {

    }

    //静态内部类 优点：加载时不会初始化静态变量INSTANCE，因为没有主动使用，达到Lazy loading
    private static class SingletonHolder {
        private final static SmsAutoUtil INSTANCE = new SmsAutoUtil();
    }

    public static SmsAutoUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void startWork(Context context, Handler handler, CustomEditTextLeftIcon et) throws Exception {
        if (mCallback == null) {
            mCallback = new MyLoaderCallback(context, et);
            ((FragmentActivity) context).getSupportLoaderManager().initLoader(0, null, mCallback);
        }
    }

    // added by randy
    public void startWork(Context context, Handler handler, EditText et) throws Exception {
        if (mCallback == null) {
            mCallback = new MyLoaderCallback(context, et, "2");
            ((FragmentActivity) context).getSupportLoaderManager().initLoader(0, null, mCallback);
        }
    }


    public void stopWork(Context context) throws Exception {
        if (mCallback != null) {
            ((FragmentActivity) context).getSupportLoaderManager().destroyLoader(0);
            mCallback = null;
        }

    }

    private class MyLoaderCallback implements LoaderCallbacks<Cursor> {
        private Context context;
        private CustomEditTextLeftIcon et;
        private CustomEditTextDel etl;
        private String mType = "0";
        private EditText et0;// added by randy

        private MyLoaderCallback(Context context, CustomEditTextLeftIcon et) {
            this.context = context;
            this.et = et;
        }

        public MyLoaderCallback(Context context, CustomEditTextDel et, String type) {
            this.context = context;
            this.etl = et;
            mType = type;
        }

        // added by randy
        public MyLoaderCallback(Context context, EditText et, String type) {
            this.context = context;
            this.et0 = et;
            this.mType = type;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
            CursorLoader cursorLoader = null;
            try {
                cursorLoader = new CursorLoader(context, Uri.parse(SMS_URL_INBOX),
                        new String[]{"_id", "address", "body", "read"}, "read=?",
                        new String[]{"0"}, "date desc");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
            if (arg1 != null) {
                arg1.moveToFirst();
                if (arg1.moveToFirst()) {
                    String smsBody = arg1.getString(arg1.getColumnIndex("body"));
                    String _id = arg1.getString(arg1.getColumnIndex("_id"));

                    //正则表达式验证验证码
                    String tag = "理财时代";
//					if(smsBody.contains(tag)){
                    String regex = "\\D\\d{6}\\D";
                    String regex2 = "\\d{6}";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(smsBody);
                    while (matcher.find()) {
                        Pattern pattern2 = Pattern.compile(regex2);
                        Matcher matcher2 = pattern2.matcher(matcher.group());
                        while (matcher2.find()) {
                            if (mType.equals("0")) {
                                et.setText(matcher2.group());
                                setSmsIsReaded(context, _id);
                            } else if (mType.equals("1")) {// added by randy
                                etl.setEdtText(matcher2.group());
                                setSmsIsReaded(context, _id);
                            } else {
                                et0.setText(matcher2.group());
                                setSmsIsReaded(context, _id);
                            }
                        }
                    }
//					}

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> arg0) {
        }

    }


    private class SmsContent extends ContentObserver {

        private Context context;
        private CustomEditTextLeftIcon et;

        public SmsContent(Context context, Handler handler, CustomEditTextLeftIcon et) {
            super(handler);
            this.context = context;
            this.et = et;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Cursor cursor = null;
            cursor = ((Activity) context).managedQuery(Uri.parse(SMS_URL_INBOX), new String[]{"_id", "address", "body", "read"},
                    "read=?", new String[]{"0"}, "date desc");
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    String smsBody = cursor.getString(cursor.getColumnIndex("body"));

                    //正则表达式验证验证码
                    String tag = "理财时代";
                    if (smsBody.contains(tag)) {
                        String regex = "\\D\\d{6}\\D";
                        String regex2 = "\\d{6}";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(smsBody);
                        while (matcher.find()) {
                            Pattern pattern2 = Pattern.compile(regex2);
                            Matcher matcher2 = pattern2.matcher(matcher.group());
                            while (matcher2.find()) {
                                et.setText(matcher2.group());
                            }
                        }
                    }

                }
            }
        }

    }


    /**
     * 将短信标记为已读
     */
    private void setSmsIsReaded(Context context, String _id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("read", "1");
        context.getContentResolver().update(Uri.parse("content://sms/inbox/" + _id), contentValues, null, null);
    }


    public void startWork(Context context, Handler handler, CustomEditTextDel et) {
        if (mCallback == null) {
            mCallback = new MyLoaderCallback(context, et, "1");
            ((FragmentActivity) context).getSupportLoaderManager().initLoader(0, null, mCallback);
        }
    }
}
