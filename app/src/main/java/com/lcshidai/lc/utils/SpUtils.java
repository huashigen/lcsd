package com.lcshidai.lc.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import com.lcshidai.lc.AtrApplication;

/**
 * SharedPreferences工具类
 */
public class SpUtils {

    public static class Table {
        // 默认sp文件名
        public static final String DEFAULT = "trj_shared";
        // 用户信息
        public static final String USER = "user_info";
        // 配置信息
        public static final String CONFIG = "config_setting";
        // 消息
        public static final String MSG = "MSG_TABLE";
        // 启动页图片
        public static final String SPLASH = "LOAD_URL_IMG";
    }

    public static class User {
        // 银行预留手机号
        public static final String BANK_LEFT_PHONE = "bank_left_phone";
        // user name
        public static final String USER_NAME = "user_name";
        // uid
        public static final String UID = "UID";
        // uc_id
        public static final String UC_ID = "uc_id";
        // 账户资金安全险状态
        public static final String IS_ACCOUNT_INSURANCE = "is_account_insurance";
        // 开通或查看账户资金安全险状态的url
        public static final String ACCOUNT_INSURANCE_URL = "account_insurance_url";
    }

    public static class Config {
        public static final String REG_FLOW = "reg_event";
        // 账户页用户数据是否可见
        public static final String IS_SHOW_ACCOUNT = "HiddenAccount";
        // 长富云理财 access_token
        public static final String ACCESS_TOKEN = "access_token";
        // 长富云理财 user_token
        public static final String USER_TOKEN = "user_token";
        // 手势密码是否关闭1-关闭;0-未关闭
        public static final String IS_GESTURE_OPEN = "is_gesture_lock_open";
        // 手势密码总共尝试次数
        public static final String TOTAL_TRY_TIMES = "gesture_surplus_times";
        // 消息推送是否开启
        public static final String IS_PUSH_OPEN = "bdpush_is_open";
        // 是否首次启动app
        public static final String IS_FIRST_OPEN = "first_start_flag";
        // 手势密码用户名
        public static final String USER_NAME = "gesture_lock_user_name";
        // 手机密码用户id
        public static final String UID = "gesture_lock_user_id";
        // version name
        public static final String VERSION_NAME = "sp_version_name";
        // version code
        public static final String VERSION_CODE = "sp_version_code";
        // Udesk domain
        public static final String U_DOMAIN = "udesk_domain";
        // Udesk sdk token
        public static final String U_SDK_TOKEN = "udesk_sdktoken";
        // Udesk app id
        public static final String U_APP_ID = "udesk_appid";
        // Udesk app key
        public static final String U_APP_KEY = "udesk_appkey";

    }

    public static class Splash {
        // 启动页图片url
        public static final String IMG_URL = "loadurl";
    }

    /**
     * @param tableName context
     * @param key       sp key
     * @param object    sp object
     */
    public static void setParam(String tableName, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editor = getSp(tableName).edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.apply();
    }

    /**
     * @param tableName     tableName
     * @param key           sp key
     * @param defaultObject sp defaultObj
     * @return sp object(type of String、Integer、Boolean, etc
     */
    public static Object getParam(String tableName, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        if ("String".equals(type)) {
            return getSp(tableName).getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return getSp(tableName).getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return getSp(tableName).getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return getSp(tableName).getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return getSp(tableName).getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public static void setInt(String tableName, String key, int value) {
        getSp(tableName).edit().putInt(key, value).apply();
    }

    public static int getInt(String tableName, String key, int defValue) {
        return getSp(tableName).getInt(key, defValue);
    }

    public static void setBoolean(String tableName, String key, boolean value) {
        getSp(tableName).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String tableName, String key, boolean defValue) {
        return getSp(tableName).getBoolean(key, defValue);
    }

    public static void setString(String tableName, String key, String value) {
        getSp(tableName).edit().putString(key, value).apply();
    }

    public static String getString(String tableName, String key) {
        return getSp(tableName).getString(key, "");
    }

    private static String setListString(List<Object> list) throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(list);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> getListString(String string) throws IOException, ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(string.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        List<Object> SceneList = (List<Object>) objectInputStream.readObject();
        objectInputStream.close();
        return SceneList;
    }

    /**
     * 保存list 数据
     *
     * @param tableName
     * @param list
     * @param key
     */
    public static void setListObj(String tableName, List list, String key) {
        Editor editor = getSp(tableName).edit();
        try {
            String str = setListString(list);
            editor.putString(key, str);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取保存的list对象
     *
     * @param tableName
     * @param key
     * @return
     */
    public static List<?> getListObj(String tableName, String key) {
        List list = new ArrayList();
        String str = getSp(tableName).getString(key, "");
        try {
            list = (List) getListString(str);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 序列化对象
     *
     * @param person
     * @return
     * @throws IOException
     */
    private static String serialize(Object person) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(person);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Object deSerialization(String str) throws IOException, ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object person = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return person;
    }

    /**
     * 将对象序列化后保存
     *
     * @param tableName
     * @param key
     * @param obj
     */
    static void setObject(String tableName, String key, Object obj) {
        try {
            String str = serialize(obj);
            getSp(tableName).edit().putString(key, str).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象
     *
     * @param tableName
     * @param key
     * @return
     */
    static Object getObject(String tableName, String key) {
        try {
            String str = getSp(tableName).getString(key, "");
            return deSerialization(str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移除某一个key所对应的值
     *
     * @param tableName
     * @param key
     */
    public static void remove(String tableName, String key) {
        Editor editor = getSp(tableName).edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 移除Sp文件里面的所有数据
     *
     * @param tableName sp 文件名
     */
    public static void clear(String tableName) {
        Editor editor = getSp(tableName).edit();
        editor.clear();
        editor.apply();
    }

    static SharedPreferences getSp(String tableName) {
        return AtrApplication.getInstance().getSharedPreferences(tableName, Context.MODE_PRIVATE);
    }
}
