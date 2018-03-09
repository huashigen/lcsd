package com.lcshidai.lc.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.BroadCastImpl;
import com.lcshidai.lc.impl.UpdateManagerImpl;
import com.lcshidai.lc.model.pub.UpdateManagerData;
import com.lcshidai.lc.model.pub.UpdateManagerJson;
import com.lcshidai.lc.service.HttpUpdateManagerService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.XML3R3Manager.ValueVersion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager implements UpdateManagerImpl {

    private static UpdateManager instance;
    HttpUpdateManagerService hums;
    private Context mContext;
    private RemoteViews notifiViews;
    private Dialog updateDialog;

    private boolean isDownloadNow = false;

    // 返回的安装包url
    private String apkUrl = "";
    private String apkName = "";
    /* 下载包安装路径 */
    private String downloadPath = "";
    private static String saveFileName = "";

    private static final int DOWN_FIRST = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    // 当前版本
    private String mVer;
    private String mPackName;
    private boolean product_trash = true; // 是否强制更新

    private NotificationManager downNotifyManager;
    private Notification downNotification;
    private NotificationCompat.Builder downNotifyBuilder;

    private VersionCheckInterface vcInterface = null;

    private UpdateManager(Context context) {
        this.mContext = context;
        mPackName = context.getPackageName();
        hums = new HttpUpdateManagerService((TRJActivity) this.mContext, this);
    }

    public static UpdateManager getInstance(Context context) {
        if (instance == null) {
            instance = new UpdateManager(context);
        }
        return instance;
    }

    Handler downloadHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            showDownloadNotification(msg.what);
        }

    };

    public static boolean askUpdate(Context context) {
        String xmlDate = (String) SpUtils.getParam(SpUtils.Table.DEFAULT, "date_update", "");
        return !TimeUtil.getCurDate().equals(xmlDate);
    }

    public static void setUpdate(Context context) {
        SpUtils.setParam(SpUtils.Table.DEFAULT, "date_update", TimeUtil.getCurDate());
    }

    public static void resetUpdate(Context context) {
        SpUtils.setParam(SpUtils.Table.DEFAULT, "date_update", "");
    }


    // 外部接口让主Activity调用（新接口）
    public void checkUpdateInfo(final Context context, final int flag) {
        try {
            vcInterface = (VersionCheckInterface) context;
        } catch (Exception e) {
        }
        mVer = getVerName(mContext, mPackName);
        hums.gainUpdateManager(mVer, context, flag);

    }

    public void showDialog() {
        if (updateDialog != null) {
            if (!updateDialog.isShowing()) {
                updateDialog.show();
            }
        }
    }

    /**
     * 判断是否要下载（判断服务器的版本大于当前版本） return true:有更新 false:无更新
     */
    private boolean hasNewVersionInServer(String currentV, String serverV) {
        try {
            String currentStr = currentV.replaceAll("\\.", "");
            String serverStr = serverV.replaceAll("\\.", "");
            int currentInt = currentStr.length();
            int serverInt = serverStr.length();
            if ((currentInt - serverInt) > 0) {
                for (int i = 0; i < (currentInt - serverInt); i++) {
                    serverStr = serverStr.concat("0");
                }
            } else if ((currentInt - serverInt) < 0) {
                for (int i = 0; i < (serverInt - currentInt); i++) {
                    currentStr = currentStr.concat("0");
                }
            }
            int cVersion = Integer.parseInt(currentStr);
            int sVersion = Integer.parseInt(serverStr);
            return cVersion < sVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 下载通知初始化
     */
    private void makeDownloadNotification() {
        downNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        downNotifyManager.cancel("update_down", 1);
        notifiViews = new RemoteViews(mContext.getPackageName(), R.layout.update_download_notification);
        notifiViews.setTextViewText(R.id.update_download_notification_title, "正在下载...0%");
        notifiViews.setTextViewText(R.id.update_download_notification_path, saveFileName);
        notifiViews.setProgressBar(R.id.update_download_notification_progress, 100, 0, false);
        notifiViews.setImageViewBitmap(R.id.update_download_notification_icon,
                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon));
        // 4.2以下版本
        if (android.os.Build.VERSION.SDK_INT < 16) {
            downNotification = new Notification();
            downNotification.flags = Notification.FLAG_NO_CLEAR;
            downNotification.when = System.currentTimeMillis();
            downNotification.tickerText = "开始下载";
            downNotification.icon = R.drawable.icon;
            downNotification.contentView = notifiViews;
            if (android.os.Build.VERSION.SDK_INT < 14) {
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        mContext, (int) System.currentTimeMillis(),
                        new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
                downNotification.contentIntent = pendingIntent;
            }
            downNotifyManager.notify("update_down", 1, downNotification);
        } else {
            downNotifyBuilder = new NotificationCompat.Builder(mContext);
            downNotifyBuilder.setOngoing(true);
            downNotifyBuilder.setTicker("开始下载");
            downNotifyBuilder.setWhen(System.currentTimeMillis());
            downNotifyBuilder.setSmallIcon(R.drawable.icon);
            downNotifyBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
            downNotifyBuilder.setContent(notifiViews);
            downNotifyManager.notify("update_down", 1, downNotifyBuilder.build());
        }
    }

    /**
     * 下载通知更新显示
     */
    private void showDownloadNotification(int flag) {

        switch (flag) {
            case DOWN_FIRST:
                makeDownloadNotification();
                break;
            case DOWN_UPDATE:
                notifiViews.setProgressBar(R.id.update_download_notification_progress, 100, progress, false);
                notifiViews.setTextViewText(R.id.update_download_notification_title, "正在下载..." + progress + "%");
                if (android.os.Build.VERSION.SDK_INT < 16) {
                    downNotifyManager.notify("update_down", 1, downNotification);
                } else {
                    downNotifyManager.notify("update_down", 1, downNotifyBuilder.build());
                }
                break;

            case DOWN_OVER:
                // 4.2以下版本
                if (android.os.Build.VERSION.SDK_INT < 16) {
                    downNotification.when = System.currentTimeMillis();
                    downNotification.tickerText = "下载完成";
                    downNotification.icon = R.drawable.icon;
                } else {
                    downNotifyBuilder.setTicker("下载完成");
                    downNotifyBuilder.setWhen(System.currentTimeMillis());
                    downNotifyBuilder.setSmallIcon(R.drawable.icon);
                }

                notifiViews.setProgressBar(R.id.update_download_notification_progress, 100, 100, false);
                Intent intent = new Intent(
                        "com.yrz.trj.action.UpdateDownloadNotificationClickReciever");
                intent.putExtra("apk_path", saveFileName);
                notifiViews.setTextViewText(
                        R.id.update_download_notification_title, "下载完成，点击安装");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                        (int) System.currentTimeMillis(), intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                if (android.os.Build.VERSION.SDK_INT < 16) {
                    // downNotification.contentIntent = pendingIntent;
                    downNotifyManager.notify("update_down", 1, downNotification);
                } else {
                    // downNotifyBuilder.setContentIntent(pendingIntent);
                    downNotifyManager.notify("update_down", 1,
                            downNotifyBuilder.build());
                }
                mContext.sendBroadcast(intent);
                break;
        }
    }

    /**
     * apk下载线程
     */
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int progressCount = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File apkFile = new File(saveFileName);
                apkFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(apkFile);
                downloadHandler.sendEmptyMessage(DOWN_FIRST);

                int count = 0;
                byte buf[] = new byte[1024];
                long startTime = 0;
                long endTime = 0;

                int numread = 0;
                int tempProgress = 0;
                while (true) {
                    numread = is.read(buf);
                    if (numread > 0) {
                        count += numread;
                        tempProgress = (int) (((float) count / progressCount) * 100);
                        endTime = System.currentTimeMillis();
                        if (endTime - startTime >= 500 && tempProgress > progress) {
                            startTime = endTime;
                            progress = tempProgress;
                            downloadHandler.sendEmptyMessage(DOWN_UPDATE);
                        }
                        fos.write(buf, 0, numread);
                    } else {
                        downloadHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                }

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                resetUpdate(mContext);
            } catch (IOException e) {
                e.printStackTrace();
                resetUpdate(mContext);
            }
            isDownloadNow = false;
        }
    };

    /**
     * 下载apk
     *
     * @param context context
     */

    private void downloadApk(Context context) {
        if (!isDownloadNow) {
            Toast.makeText(mContext, "开始下载...", Toast.LENGTH_SHORT).show();
            if (createSDCardDir()) {
                isDownloadNow = true;
                progress = 0;
                downLoadThread = new Thread(mdownApkRunnable);
                downLoadThread.start();
            }
        } else {
            Toast.makeText(mContext, "已经开始下载，请稍候", Toast.LENGTH_SHORT).show();
        }
        if (product_trash) {
            Intent intent = new Intent();
            intent.setAction(BroadCastImpl.MY_ACTION);
            intent.putExtra(BroadCastImpl.MY_RESON, BroadCastImpl.EXIT_EXTRA);
            context.sendBroadcast(intent);
        }
    }

    /**
     * 获取当前版本
     *
     * @param context
     * @param packName
     * @return
     */
    public static String getVerName(Context context, String packName) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(packName, 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e("Update", e.getMessage());
        }
        return verName;
    }

    /**
     * 创建下载目录
     */
    public boolean createSDCardDir() {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                // 创建一个文件夹对象，赋值为外部存储器的目录
                File sdcardDir = Environment.getExternalStorageDirectory();
                // 得到一个路径，内容是sdcard的文件夹路径和名字
                downloadPath = sdcardDir.getPath() + "/trj/Download/";
                File file_path = new File(downloadPath);
                if (!file_path.exists()) {
                    // 若不存在，创建目录，可以在应用启动的时候创建
                    file_path.mkdirs();
                }
                saveFileName = downloadPath + apkName;
                return true;
            } else {
                Toast.makeText(mContext, "sd卡出错", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "下载出错", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private Dialog makeUpdateDialog(final Context context, final int flag, String size,
                                    String vision, String message) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
//		View view = getView(mContext , R.layout.layout_dialog_update);
//        View view = layoutInflater.inflate(R.layout.layout_dialog_update, null);
//        LinearLayout liearTitle = (LinearLayout) view.findViewById(R.id.linear_title);
        View view = layoutInflater.inflate(R.layout.layout_dialog_update_1, null);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.new_version);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_version);
        TextView titleTV = (TextView) view.findViewById(R.id.dialog_nomessage_tv_title);
        TextView absoluteBT = (TextView) view.findViewById(R.id.dialog_nomessage_bt_absolute);
//        TextView negativeBT = (TextView) view.findViewById(R.id.dialog_nomessage_bt_negative);
        ImageView negativeBT = (ImageView) view.findViewById(R.id.dialog_nomessage_bt_negative);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = (int) (screenWidth * 0.85);
//        liearTitle.setLayoutParams(lp);
        relativeLayout.setLayoutParams(lp);

//		if (android.os.Build.VERSION.SDK_INT < 12) {
//			negativeBT
//					.setBackgroundResource(R.drawable.bg_dialog_button_absolute_12);
//			absoluteBT
//					.setBackgroundResource(R.drawable.bg_dialog_button_negative_12);
//		} else {
//
//			negativeBT
//					.setBackgroundResource(R.drawable.bg_dialog_button_absolute);
//			absoluteBT
//					.setBackgroundResource(R.drawable.bg_dialog_button_negative);
//		}
        tv_title.setText("v" + vision);
        titleTV.setText(message);
//		absoluteBT.setText("去升级");
//		negativeBT.setText("忽略");
        absoluteBT.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setUpdate(context);
                downloadApk(context);
                if (updateDialog.isShowing()) {
                    updateDialog.dismiss();
                }
            }
        });

        negativeBT.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setUpdate(mContext);
                if (updateDialog.isShowing()) {
                    updateDialog.dismiss();

                    if (product_trash) {
//						Intent intent = new Intent();
//						intent.setAction(BroadCastImpl.MY_ACTION);
//						intent.putExtra(BroadCastImpl.MY_RESON,BroadCastImpl.EXIT_EXTRA);
//						context.sendBroadcast(intent);
                        BaseAppManager.getInstance().clear();
                        System.exit(0);
                    }
                }
            }
        });

        Dialog dialog = new Dialog(mContext, R.style.style_loading_dialog);
        dialog.setContentView(view);
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_SEARCH;
            }
        });
        return dialog;
    }

    /**
     * 重置update manager对象
     */
    public static void reset() {
        instance = null;
    }

    interface VersionCheckInterface {
        // 0:无需更新 1:需要更新
        void versionCheckResult(String isNeedUpdate);
    }

    @Override
    public void gainUpdateManagersuccess(UpdateManagerJson response, Context context, int flag) {
        try {
            if (response != null && response.getBoolen() == 1) {
                UpdateManagerData jObj = response.getData();
                // 0:无需更新 1:需要更新
                String isUpdate = jObj.getUpdate();
                if (jObj.getProduct_trash() != null) {
                    product_trash = "1".equals(jObj.getProduct_trash());
                }
                if (null != vcInterface) {
                    vcInterface.versionCheckResult(isUpdate);
                }
                String crc = jObj.getCrc();
                String version = jObj.getVersion();
                ValueVersion vmodel = new XML3R3Manager().new ValueVersion();
                vmodel.value = crc;
                vmodel.version = version;
                XML3R3Manager.saveXml(context, vmodel);
                if ("1".equals(isUpdate)) {
                    apkUrl = jObj.getDownload();
//					apkUrl = LCHttpClient.URL_DOWN_APK;
                    apkName = "trj_android_" + jObj.getVersion() + ".apk";
                    String size = jObj.getSize();
                    if (updateDialog == null) {
                        updateDialog = makeUpdateDialog(context, flag, size,
                                version, response.getData().getAndroid_remark());
                    }
                    if (!updateDialog.isShowing()) {
                        updateDialog.show();
                    }
                } else {
                    if (flag == 1) {
                        ToastUtil.showToast((Activity) mContext, "当前已是最新版本");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainUpdateManagerfail() {

    }
}
