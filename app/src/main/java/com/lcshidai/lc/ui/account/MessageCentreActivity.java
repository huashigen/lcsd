package com.lcshidai.lc.ui.account;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.entity.MessageInfo;
import com.lcshidai.lc.impl.account.MessageCentreImpl;
import com.lcshidai.lc.model.Page;
import com.lcshidai.lc.model.account.MessageCentreData;
import com.lcshidai.lc.model.account.MessageCentreInfo;
import com.lcshidai.lc.model.account.MessageCentreJson;
import com.lcshidai.lc.service.account.HttpMessageCentreService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.TimeUtil;
import com.lcshidai.lc.widget.ppwindow.MessageCentrePopupWindow;
import com.lcshidai.lc.widget.xlvfresh.XListView;
import com.lcshidai.lc.widget.xlvfresh.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息中心
 *
 * @author 000853
 */
public class MessageCentreActivity extends TRJActivity implements
        MessageCentreImpl, OnClickListener, IXListViewListener,
        MessageCentrePopupWindow.OnTypeSelectedListener, OnItemClickListener {
    HttpMessageCentreService hmcs;
    private int nowPage = 1; // 当前页数
    private static int AVG_PAGE_NUM = 10; // 每页的个数
    private int totalPage; // 总页数（从服务器获得）
    private boolean isLoading = false; // 是否正在加载数据

    private RelativeLayout rlTopLeftContainer, rlTopRightContainer;
    private TextView tvTopLeft, tvMid;
    private TextView tvTopRight;
    private ImageView ivTopLeft;
    private Context mContext;

    private SharedPreferences mSharedPreferences;

    private XListView message_lv;
    private MessageCentrePopupWindow mcPop;
    private RelativeLayout emptyRL;
    private Dialog loading;
    private List<MessageInfo> msgInfoList;
    private MessageCentreAdapter msgAdapter;

    private final String[] titleArray = new String[]{"全部", "开标提醒", "投资成功",
            "到期提醒"/*, "变现提醒"*/, "回款提醒", "活动奖励"/*, "预约提醒"*/};

    boolean hasMore = false;
    private boolean isDown;
    private int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initView();
        hmcs = new HttpMessageCentreService(this, this);
        mSharedPreferences = getSharedPreferences(MemorySave.MS.userInfo.uname, Context.MODE_PRIVATE);
        loadData(selectedPosition);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_message_center);
        rlTopLeftContainer = (RelativeLayout) findViewById(R.id.rl_top_left_container);
        rlTopRightContainer = (RelativeLayout) findViewById(R.id.rl_top_right_container);
        ivTopLeft = (ImageView) findViewById(R.id.iv_top_left);
        ivTopLeft.setOnClickListener(this);
        tvTopRight = (TextView) findViewById(R.id.tv_top_right);
        tvTopRight.setText("筛选");
        tvTopRight.setVisibility(View.VISIBLE);
        tvMid = (TextView) findViewById(R.id.tv_mid);
        tvMid.setText("消息中心");

        loading = createLoadingDialog(mContext, "加载中", true);
        loading.show();
        msgInfoList = new ArrayList<MessageInfo>();

        rlTopLeftContainer.setOnClickListener(this);
        ivTopLeft.setOnClickListener(this);
        rlTopRightContainer.setOnClickListener(this);
        tvTopRight.setOnClickListener(this);

        emptyRL = (RelativeLayout) findViewById(R.id.rl_empty);
        message_lv = (XListView) findViewById(R.id.helpcenter_lv_helplist);
        message_lv.setXListViewListener(this);
        message_lv.setPullLoadEnable(false);
        msgAdapter = new MessageCentreAdapter();
        message_lv.setAdapter(msgAdapter);
        message_lv.setOnItemClickListener(this);

        mcPop = new MessageCentrePopupWindow(mContext);
        mcPop.setOnTypeSelectedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 == 0 || arg2 > msgInfoList.size())
            return;
        MessageInfo msg = msgInfoList.get(arg2 - 1);
        // 消息状态更新到已读
        mSharedPreferences.edit().putInt(msg.id, 1).commit();
        msgAdapter.notifyDataSetChanged();
        Intent intent = new Intent(mContext, MessageDetailActivity.class);
        intent.putExtra("message_info", msg);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_top_left:
            case R.id.rl_top_left_container:
                MessageCentreActivity.this.finish();
                break;
            case R.id.rl_top_right_container:
            case R.id.tv_top_right:
                mcPop.setCurrentId(selectedPosition);
                mcPop.showAsDown(rlTopRightContainer);
//                mcPop.showAsDropDown(rlTopLeftContainer);
                break;
        }
    }

    @Override
    public void onSelected(int id, int position) {
        loading.show();
        tvTopRight.setText(titleArray[position]);
        selectedPosition = id;
        msgInfoList.clear();
        nowPage = 1;
        loadData(id);
    }

    class ViewHolder {
        View v_read;
        TextView type_tv;
        TextView time_tv;
        TextView info_tv;
    }

    class MessageCentreAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return msgInfoList.size() + (hasMore ? 1 : 0);
        }

        @Override
        public Object getItem(int position) {
            return msgInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (position < msgInfoList.size()) {
                MessageInfo info = (MessageInfo) getItem(position);
                if (null == convertView || null == convertView.getTag()) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.activity_message_centre_list_item, null);
                    holder.v_read = convertView.findViewById(R.id.v_read);
                    holder.type_tv = (TextView) convertView
                            .findViewById(R.id.message_centre_list_item_tv_type);
                    holder.time_tv = (TextView) convertView
                            .findViewById(R.id.message_centre_list_item_tv_time);
                    holder.info_tv = (TextView) convertView
                            .findViewById(R.id.message_centre_list_item_tv_info);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.type_tv.setText(info.remind_title);
                holder.time_tv.setText(info.show_time);
                holder.info_tv.setText(info.remind_message);
                // todo 已读／未读
                if (mSharedPreferences.getInt(info.id, 0) == 0) {// 未读
                    holder.info_tv.setTextColor(getResources().getColor(R.color.black));
                    holder.info_tv.setMaxLines(1);
                } else {// 已读
                    holder.info_tv.setTextColor(getResources().getColor(R.color.tv_default));
                    holder.info_tv.setMaxLines(2);
                }
                String show_time = TimeUtil.dateShowStyleConvert(info.show_time, "yyyy/MM/dd HH:mm", "yyyy-MM-dd HH:mm");
                if (TimeUtil.isDateStrToday(show_time)) {
                    holder.time_tv.setText(TimeUtil.dateShowStyleConvert(show_time, "yyyy-MM-dd HH:mm", "HH:mm"));
                } else {
                    holder.time_tv.setText(TimeUtil.dateShowStyleConvert(info.show_time, "yyyy/MM/dd HH:mm", "yy/MM/dd"));
                }
            } else {
                View view = View.inflate(MessageCentreActivity.this, R.layout.loading_item, null);
                loadData(selectedPosition);
                return view;
            }

            return convertView;
        }

    }

    @Override
    public void onRefresh() {
        isDown = true;
        nowPage = 1;
        loadData(selectedPosition);
    }

    @Override
    public void onLoadMore() {

    }

    private void loadData(int type) {
        hmcs.gainMessageCentre(type, nowPage, AVG_PAGE_NUM);

    }

    @Override
    public void gainMessageCentresuccess(MessageCentreJson response) {
        try {
            hasMore = false;
            if (loading.isShowing()) {
                loading.dismiss();
            }
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {

                    if (isDown) {
                        isDown = false;
                        msgInfoList.clear();
                        message_lv.stopRefresh();
                        message_lv.stopLoadMore();
                        message_lv.setRefreshTime();
                    }

                    MessageCentreData dataObj = response.getData();
                    Page pageObj = dataObj.getPagedata();
                    totalPage = pageObj.getTotalPages();
                    String current_page = pageObj.getCurrentPage();
                    if (current_page.equals(String.valueOf(nowPage))) {
                        List<MessageCentreInfo> dataArray = dataObj
                                .getInfodata();
                        for (int i = 0; i < dataArray.size(); i++) {
                            MessageCentreInfo obj = dataArray.get(i);
                            MessageInfo msgInfo = new MessageInfo();
                            msgInfo.id = obj.getId();
                            msgInfo.remind_title = obj.getRemind_title();
                            msgInfo.remind_message = obj.getRemind_message();
                            msgInfo.show_time = obj.getShow_time();
                            msgInfo.remind_type = obj.getRemind_type();
                            msgInfo.prj_type = obj.getPrj_type();
                            msgInfo.prj_id = obj.getPrj_id();
                            msgInfoList.add(msgInfo);
                            // 如果消息保存的消息阅读状态为0，表示是未读消息，需要设置
                            if (mSharedPreferences.getInt(obj.getId(), 0) == 0) {
                                mSharedPreferences.edit().putInt(obj.getId(), 0).commit();
                            }
                        }

                        if (totalPage > nowPage) {
                            hasMore = true;
                        }
                        nowPage += 1;
                        isLoading = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msgInfoList.size() == 0) {
                message_lv.setVisibility(View.GONE);
                emptyRL.setVisibility(View.VISIBLE);
            } else {
                message_lv.setVisibility(View.VISIBLE);
                if (emptyRL.getVisibility() == View.VISIBLE) {
                    emptyRL.setVisibility(View.GONE);
                }
            }
            msgAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void gainMessageCentrefail() {
        isLoading = false;
        showToast("网络不给力");
        if (loading.isShowing()) {
            loading.dismiss();
        }
        if (null != loading && loading.isShowing()) {
            loading.dismiss();
        }
        if (null == msgInfoList) {
            msgInfoList = new ArrayList<MessageInfo>();
        }
        if (msgInfoList.size() == 0) {
            emptyRL.setVisibility(View.VISIBLE);
        }
    }
}
