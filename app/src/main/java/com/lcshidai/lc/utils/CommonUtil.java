package com.lcshidai.lc.utils;

import android.content.ClipData;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.rytong.czfinancial.view.ZSBank;
import com.lcshidai.lc.BuildConfig;
import com.lcshidai.lc.model.OpenAccountData;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 通用工具类
 * <p/>
 * Created by Randy on 2016/5/13.
 */
public class CommonUtil {

    /**
     * 清空所有Sp文件
     */
    public static void clearAllSpFiles() {
        SpUtils.clear(SpUtils.Table.DEFAULT);
        SpUtils.clear(SpUtils.Table.USER);
        SpUtils.clear(SpUtils.Table.CONFIG);
        SpUtils.clear(SpUtils.Table.MSG);
        SpUtils.clear(SpUtils.Table.SPLASH);
    }

    /**
     * 获取当前版本CODE
     *
     * @param context  上下文对象
     * @param packName 包名
     * @return 版本号
     */
    public static int getVersionCode(Context context, String packName) {
        int verCode = 0;
        try {
            verCode = context.getPackageManager().getPackageInfo(packName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            android.util.Log.e("Update", e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context  上下文对象
     * @param packName 包名
     * @return 版本名称
     */
    public static String getVersionName(Context context, String packName) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(packName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            android.util.Log.e("Update", e.getMessage());
        }
        return versionName;
    }

    /**
     * 获取TD渠道名
     *
     * @param context  上下文对象
     * @param packName 包名
     * @return 渠道名
     */
    public static String getTDChannel(Context context, String packName) {
        String tdChannel = "";
        try {
            tdChannel = context.getPackageManager().getApplicationInfo(packName,
                    PackageManager.GET_META_DATA).metaData.getString("TD_CHANNEL_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return tdChannel;
    }

    /**
     * 获取UM渠道名
     *
     * @param context  上下文对象
     * @param packName 包名
     * @return 渠道名
     */
    public static String getUMChannel(Context context, String packName) {
        String umChannel = "";
        try {
            umChannel = context.getPackageManager().getApplicationInfo(packName,
                    PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return umChannel;
    }

    /**
     * 获取本机deviceId
     *
     * @param context 上下文对象
     * @return deviceId 或者说deviceID
     */
    public static String getDeviceId(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * crc循环冗余校验
     *
     * @param context 上下文
     */
    public static void crcCheck(Context context) {
        if (BuildConfig.DEBUG)
            return;
        String crcValue = getCrcValue(context.getPackageCodePath());
        XML3R3Manager.ValueVersion vmodel = XML3R3Manager.getCrcmodel(context);
        PackageInfo pack_info = null;
        try {
            pack_info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pack_info.versionName;
            if (!vmodel.value.equals("") && vmodel.version.equals(version) && !crcValue.equals(vmodel.value)) {// 判断程序包被篡改逻辑：获取的crc值不为空并且获取的版本号和当前运行的版本号一致而crc值与服务器不一致
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Apk路径获取循环冗余校验值
     *
     * @param apkPath apk路径
     * @return 循环冗余校验值
     */
    public static String getCrcValue(String apkPath) {
        try {
            ZipFile zipfile = new ZipFile(apkPath);
            StringBuffer crcNumber = new StringBuffer("");
            for (Enumeration entries = zipfile.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.getName().equals("AndroidManifest.xml") || entry.getName().equals("META-INF/MANIFEST.MF")
                        || entry.getName().equals("META-INF/CERT.SF") || entry.getName().equals("META-INF/CERT.RSA")) {
                    System.out.println(entry.getName() + ":" + entry.getCrc());
                    continue;
                }
                crcNumber.append(entry.getCrc() + "");
            }
            return MD5.generate(crcNumber.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断字符串是否为null或空
     */
    public static boolean isNullOrEmpty(String string) {
        return null == string || string.isEmpty() || string.equals("null");
    }

    public static String customFormat(String pattern, double value) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        return output;
    }

    public static String formatPhone(String phone) {
        if (!CommonUtil.isNullOrEmpty(phone)) {
            return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        }
        return "";
    }

    public static String formatBankCardNo(String bankCardNo) {
        int length = bankCardNo.length();
        return bankCardNo.substring(0, 5) + "*********" + bankCardNo.substring(length - 4, length);
    }

    /**
     * unix 时间戳转换成日期字符串
     *
     * @param timeStamp
     * @return
     */
    public static String timeStamp2DateString(String timeStamp) {
        String dateStr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        long stamp = Long.parseLong(timeStamp);
        dateStr = format.format(stamp);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Format To String(Date):" + dateStr);
        System.out.println("Format To Date:" + date);
        return dateStr;
    }

    /**
     * 开户
     *
     * @param openAccountData 开户信息
     */
    public static void openAccount(Context context, OpenAccountData openAccountData) {
        if (null != openAccountData) {
            Map<String, String> map = convertAccountData2Map(openAccountData);
            boolean result = ZSBank.onEvent(context, "0001", map);
        }
    }

    public static Map<String, String> convertAccountData2Map(OpenAccountData openAccountData) {
        Map<String, String> map = new HashMap<String, String>();
        if (openAccountData != null) {
            if (!CommonUtil.isNullOrEmpty(openAccountData.getVersion())) {
                map.put("version", openAccountData.getVersion());
            } else {
                map.put("version", "");
            }
            if (!CommonUtil.isNullOrEmpty(openAccountData.getInstId())) {
                map.put("instId", openAccountData.getInstId());
            } else {
                map.put("instId", "");
            }

            if (!CommonUtil.isNullOrEmpty(openAccountData.getCertId())) {
                map.put("certId", openAccountData.getCertId());
            } else {
                map.put("certId", "");
            }

            if (!CommonUtil.isNullOrEmpty(openAccountData.getDate())) {
//                String dateStr = CommonUtil.timeStamp2DateString(openAccountData.getDate());
                map.put("date", openAccountData.getDate());
            } else {
                map.put("date", "");
            }

            if (!CommonUtil.isNullOrEmpty(openAccountData.getAccountName())) {
                map.put("accountName", openAccountData.getAccountName());
            } else {
                map.put("accountName", "");
            }

            if (!CommonUtil.isNullOrEmpty(openAccountData.getCertType())) {
                map.put("certType", openAccountData.getCertType());
            } else {
                map.put("certType", "");
            }
            if (!CommonUtil.isNullOrEmpty(openAccountData.getCertNo())) {
                map.put("certNo", openAccountData.getCertNo());
            } else {
                map.put("certNo", "");
            }
            if (!CommonUtil.isNullOrEmpty(openAccountData.getMobile())) {
                map.put("mobile", openAccountData.getMobile());
            } else {
                map.put("mobile", "");
            }
            if (!CommonUtil.isNullOrEmpty(openAccountData.getCstno())) {
                map.put("Cstno", openAccountData.getCstno());
            } else {
                map.put("Cstno", "");
            }
            if (!CommonUtil.isNullOrEmpty(openAccountData.getExtention())) {
                map.put("extension", openAccountData.getExtention());
            } else {
                map.put("extension", "");
            }
            if (!CommonUtil.isNullOrEmpty(openAccountData.getSign())) {
                map.put("sign", openAccountData.getSign());
            } else {
                map.put("sign", "");
            }
            for (String key : map.keySet()) {
                Log.e("CgResultStr", key + ":" + map.get(key));
            }
        }

        return map;
    }

    @SuppressWarnings("deprecation")
    public static void copyText(String text, Context context) {
        if (Build.VERSION.SDK_INT >= 11) {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("account_info", text);
            clipboardManager.setPrimaryClip(clipData);
        } else {
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        }
    }

    public static boolean isPercentageNum(String str) {
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d*\\%?$");
        Matcher isPercentage = pattern.matcher(str);
        return isPercentage.matches();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher isNumeric = pattern.matcher(str);
        return isNumeric.matches();
    }
}
