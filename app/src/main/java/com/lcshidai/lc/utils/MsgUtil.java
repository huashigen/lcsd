package com.lcshidai.lc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;

import com.lcshidai.lc.model.MessageData;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.model.MessageType;
import com.lcshidai.lc.model.MessageTypeNew;
import com.lcshidai.lc.model.MsgNew;

/**
 * V1.0.4版本新增
 *
 * @author lfq
 */
public class MsgUtil {
    // 消息广播Action-新增
    public static final String MSG_ACTION_ADD = "MAIN_BOTTOM_MESSAGE_ACTION_ADD";
    // 消息广播Action-减少
    public static final String MSG_ACTION_REMOVE = "MAIN_BOTTOM_MESSAGE_ACIOTN_REMOVE";
    // 消息广播Action-刷新
    public static final String MSG_ACTION_REFRESH = "MAIN_BOTTOM_MESSAGE_ACTION_REFRESH";
    // 获取全部消息key
    public static final String MSG_DATA_LIST = "msg_data_list";
    // 获取用户
    public static final String UID = "uid";
    // 理财消息数量 Key
    public static final String INVEST = "invest";
    // 账户消息数量 Key
    public static final String ACCOUNT = "account";
    // 发现消息数量 Key
    public static final String DISCOVERY = "discovery";
    // 投资消息标识
    public static final String INVEST_SEQUENCE = "invest_sequence";
    // 账户消息标识
    public static final String ACCOUNT_SEQUENCE = "account_sequence";
    // 发现消息标识
    public static final String DISCOVERY_SEQUENCE = "discovery_sequence";
    // 热门活动
    public static final String TYPE_INVEST_HOT = "invest.hot_activity";
    // 公告
    public static final String TYPE_INVEST_NOTICE = "invest.notice";
    // 发现.投资者关系.媒体报道
    public static final String TYPE_DISCOVERY_REPORT = "discovery.investor_relations.report";
    // 发现.投资者关系.平台公告
    public static final String TYPE_DISCOVERY_NOTICE = "discovery.investor_relations.notice";
    // 发现.投资者关系.最新动态
    public static final String TYPE_DISCOVERY_INFORMATION = "discovery.investor_relations.information";
    // 发现.投资者关系.系统通知
    public static final String TYPE_DISCOVERY_SYSYNOTIFY = "discovery.investor_relations.sysnotify";
    // 发现.投资者关系.运营数据
    public static final String TYPE_DISCOVERY_OPTDATA = "discovery.investor_relations.optdata";
    // 红包
    public static final String TYPE_ACCOUNT_AWARDS_BONUS = "account.awards.bonus";
    // 满减券
    public static final String TYPE_ACCOUNT_AWARDS_REDUCETICKET = "account.awards.reduceticket";
    // 其他
    public static final String TYPE_ACCOUNT_AWARDS_OTHERS = "account.awards.others";
    // 加息券
    public static final String TYPE_ACCOUNT_AWARDS_RATETICKET = "account.awards.rateticket";
    // 理财金
    public static final String TYPE_ACCOUNT_TYJ = "account.tyj";
    // 邀请用户
    public static final String TYPE_ACCOUNT_USERRECOMMED = "account.userrecommed";

    // 存储消息的表名
    public static final String MSG_TABLE = "MSG_TABLE";

    public enum Type {
        invest, account, discovery
    }

    /**
     * 保存对象
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setObj(Context context, String key, Object object) {
        MessageLocalData data = (MessageLocalData) object;
        MessageLocalData localData = (MessageLocalData) getObj(context, key);//本地数据

        if (data == null) {
            return;
        }
        if (MsgUtil.ACCOUNT.equals(key)) {
            if (localData != null && data.getUid().equals(localData.getUid())) {
                Set<String> set = data.getMap().keySet();
                for (String k : set) {
                    MessageTypeNew type = data.getMap().get(k);
                    List<MsgNew> msgs = type.getMessages();
                    if (msgs != null) {
                        List<String> strs = new ArrayList<String>();
                        for (MsgNew msg : msgs) {
                            strs.add(msg.getMsg());
                        }
                        //从本地添加数据
                        MessageTypeNew messageTypeNew = localData.getMap().get(k);
                        if (messageTypeNew != null) {
                            List<MsgNew> msgNews = messageTypeNew.getMessages();
                            for (MsgNew msgNew : msgNews) {
                                if (!strs.contains(msgNew.getMsg()) && !msgNew.isDirty()) {
                                    msgs.add(msgNew);
                                    type.setCount(type.getCount() + 1);
                                    data.setCount(data.getCount() + 1);
                                }
                            }
                        }
                    }
                }
            }
            SpUtils.setObject(SpUtils.Table.MSG, key, data);
        } else {
            //对比本地数据，设置为脏数据
            if (localData != null) {
                Set<String> set = data.getMap().keySet();
                for (String k : set) {
                    MessageTypeNew type = data.getMap().get(k);
                    List<MsgNew> msgs = type.getMessages();
                    MessageTypeNew messageTypeNew = localData.getMap().get(k);
                    if (messageTypeNew != null) {
                        List<MsgNew> msgNews = messageTypeNew.getMessages();
                        for (MsgNew msgNew : msgNews) {
                            for (MsgNew msg : msgs) {
                                if (msgNew.getMsg().equals(msg.getMsg()) && msgNew.isDirty()) {
                                    msg.setDirty(true);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            SpUtils.setObject(SpUtils.Table.MSG, key, data);
        }

    }

    /**
     * 获取本地保存数据
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getObj(Context context, String key) {
        return SpUtils.getObject(SpUtils.Table.MSG, key);
    }

    public static void setString(Context context, String key, String value) {
        SpUtils.getSp(SpUtils.Table.MSG).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        return SpUtils.getSp(SpUtils.Table.MSG).getString(key, "");
    }

    public static void setInt(Context context, String key, int value) {
        SpUtils.getSp(SpUtils.Table.MSG).edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key) {
        return SpUtils.getSp(SpUtils.Table.MSG).getInt(key, -1);
    }

    public static void remove(Context context, String key) {
        SpUtils.remove(SpUtils.Table.MSG, key);
    }

    /**
     * 转换List数据类型
     *
     * @param list 请求接口获取的消息列表
     * @return 本地消息列表
     */
    public static List<MessageLocalData> convertList(List<MessageData> list) {
        List<MessageLocalData> localList = null;
        if (list != null && list.size() > 0) {
            localList = new ArrayList<>();
            for (MessageData data : list) {
                MessageLocalData localData = new MessageLocalData();
                localData.setUid(data.getUid());
                localData.setCount(data.getCount());
                localData.setType(data.getType());
                localData.setSequence(data.getSequence());
                Map<String, MessageTypeNew> map = new HashMap<String, MessageTypeNew>();
                List<MessageTypeNew> newTypes = new ArrayList<MessageTypeNew>();
                List<MessageType> types = data.getMessages();
                for (MessageType messageType : types) {
                    MessageTypeNew messageTypeNew = new MessageTypeNew();
                    messageTypeNew.setCount(messageType.getCount());
                    messageTypeNew.setType(messageType.getType());
                    messageTypeNew.setSequence(messageType.getSequence());
                    List<String> messages = messageType.getMessages();
                    List<MsgNew> msgNews = new ArrayList<MsgNew>();
                    for (String string : messages) {
                        msgNews.add(new MsgNew(string, false));
                    }
                    messageTypeNew.setMessages(msgNews);
                    newTypes.add(messageTypeNew);
                }
                if (newTypes.size() > 0) {
                    for (MessageTypeNew newType : newTypes) {
                        map.put(newType.getType(), newType);
                    }
                }
                localData.setMap(map);
                localList.add(localData);
            }
        }
        return localList;
    }

    /**
     * 初始化本地消息数据
     *
     * @param context context
     * @param list    本地消息列表
     */
    public static void initData(Context context, List<MessageLocalData> list) {
        if (list != null && list.size() > 0) {
            for (MessageLocalData data : list) {
                if (data == null) return;
                int position = -1;
                if (MsgUtil.INVEST.equals(data.getType())) {
                    MsgUtil.setObj(context, MsgUtil.INVEST, data);
                    MsgUtil.setInt(context, MsgUtil.INVEST_SEQUENCE, data.getSequence());
                    position = 0;
                } else if (MsgUtil.ACCOUNT.equals(data.getType())) {
                    MsgUtil.setObj(context, MsgUtil.ACCOUNT, data);
                    MsgUtil.setInt(context, MsgUtil.ACCOUNT_SEQUENCE, data.getSequence());
                    position = 1;
                } else if (MsgUtil.DISCOVERY.equals(data.getType())) {
                    MsgUtil.setObj(context, MsgUtil.DISCOVERY, data);
                    MsgUtil.setInt(context, MsgUtil.DISCOVERY_SEQUENCE, data.getSequence());
                    position = 2;
                }
                Intent intent = new Intent();
                intent.setAction(MsgUtil.MSG_ACTION_REFRESH);
                intent.putExtra("flag", position);
                context.sendBroadcast(intent);
            }
        }
    }
}
