package com.lcshidai.lc.ui.fragment.finance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.HotInfoMovementImpl;
import com.lcshidai.lc.model.MessageLocalData;
import com.lcshidai.lc.model.MessageTypeNew;
import com.lcshidai.lc.model.MsgNew;
import com.lcshidai.lc.model.finance.FinaceInfoMovementData;
import com.lcshidai.lc.model.finance.HotInfoMovementData;
import com.lcshidai.lc.model.finance.HotInfoMovementJson;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.service.finance.HttpHotMovementService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.MsgUtil;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;
import com.lcshidai.lc.widget.ppwindow.ProfileInvestPopupWindow;
import com.lcshidai.lc.widget.xlvfresh.XListView;
import com.lcshidai.lc.widget.xlvfresh.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lcshidai.lc.R.id.iv_activity;

/**
 * 理财列表子类-除了精选的其他类型
 *
 * @author 000814
 */
public class FinaceMovementChildFragment extends TRJFragment
        implements IXListViewListener, HotInfoMovementImpl {

    private XListView mListView;
    private ItemAdapter mAdapter;
    private View mContent;
    private int mPagesize = 10;
    private int mP = 1;
    boolean hasMore;

    public boolean isRundata = false;
    public String mCurrentPType = "0";
    public String filter = "";
    ProfileInvestPopupWindow pipw;
    PayPasswordPopupWindow pw;
    private int screenWidth;

    public HashMap<String, TVIDTime> startTVMap = new HashMap<String, TVIDTime>();
    public ArrayList<String> idList = new ArrayList<String>();
    View mProgressBar;
    private String type;
    private FinanceInvestPBuyCheckData mymodel;

    private MessageLocalData msgData;
    private MessageTypeNew msgType;

    HttpHotMovementService mHttpService;

    public FinaceMovementChildFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (null != data) {
            type = data.getString("type");
        }
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        mHttpService = new HttpHotMovementService((TRJActivity) getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        热门活动布局
        mContent = inflater.inflate(R.layout.fragment_lisvt_view, container, false);
        mAdapter = new ItemAdapter(getActivity());
        mProgressBar = mContent.findViewById(R.id.progressContainer);
        mListView = (XListView) mContent.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(false);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        mListView.setXListViewListener(this);

        View mainView = mContent.findViewById(R.id.ll_main);
        mProgressBar.setVisibility(View.GONE);
        reLoadData();

        return mContent;
    }

    public void ld(boolean isReload, String ctype) {
        if (!isReload && mAdapter != null && mAdapter.size > 1) {
            type = ctype;
            mListView.showHeader();
        } else {
            type = ctype;
            reLoadData();
        }
    }

    public synchronized void reLoadData() {
        if (mAdapter != null && !isRundata) {
            startTVMap.clear();
            idList.clear();
            hasMore = false;
            mAdapter.clear();
            mListView.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            mP = 1;
            mProgressBar.setVisibility(View.VISIBLE);
            mContent.findViewById(R.id.rl_empty).setVisibility(View.GONE);
            loadData();
        }
    }

    public synchronized void reLoadDataDown() {
        if (mAdapter != null && !isRundata) {
            startTVMap.clear();
            idList.clear();
            isDown = true;
            hasMore = false;
            mP = 1;
            loadData();
        }
    }

    boolean isDown = false;

    private void loadData() {
        if (getActivity() == null || !NetUtils.isNetworkConnected(getActivity())) {
            mProgressBar.setVisibility(View.GONE);
            mContent.findViewById(R.id.listView).setVisibility(View.GONE);
            mContent.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
            return;
        }
        isRundata = true;
        mHttpService.gainHotInfo(type, String.valueOf(mP), String.valueOf(mPagesize));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        isRundata = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (MemorySave.MS.mGoFinancetype != -1) {
            MemorySave.MS.mGoFinancetype = -1;
            return;
        }

        msgData = (MessageLocalData) MsgUtil.getObj(getActivity(), MsgUtil.INVEST);
        if (msgData != null) {
            if (msgData.getMap() != null && msgData.getMap().size() > 0) {
                msgType = msgData.getMap().get(MsgUtil.TYPE_INVEST_HOT);
            }
        }
    }

    public class ItemAdapter extends ArrayAdapter<HotInfoMovementData> {
        private Context mContext;
        public List<HotInfoMovementData> mfilelist = new ArrayList<HotInfoMovementData>();
        public int size;

        public ItemAdapter(Activity context) {
            super(context, 0);
            mContext = context;
        }

        @Override
        public int getCount() {
            size = mfilelist.size();
            return size + (hasMore ? 1 : 0);
        }

        @Override
        public void add(HotInfoMovementData object) {
            mfilelist.add(object);
        }

        @Override
        public void clear() {
            mfilelist.clear();
            super.clear();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public HotInfoMovementData getItem(int position) {
            return mfilelist.get(position);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            try {
                final ViewHolders vh;
                if (position < size) {
                    vh = new ViewHolders();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_movement_child, null);
                    vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                    vh.tv_time1 = (TextView) convertView.findViewById(R.id.tv_time1);
                    vh.iv_used = (RelativeLayout) convertView.findViewById(R.id.iv_used);
                    vh.rl_time = (RelativeLayout) convertView.findViewById(R.id.rl_time);
                    vh.iv_activity = (ImageView) convertView.findViewById(iv_activity);
                    vh.iv_newmsg = (ImageView) convertView.findViewById(R.id.iv_newmsg);
                    final HotInfoMovementData pi = mfilelist.get(position);

                    vh.tv_time.setText(pi.getName());
                    if (pi.getActivity_status().equals("1")) {
//						vh.rl_time.setVisibility(View.VISIBLE);
                        vh.iv_used.setVisibility(View.GONE);
                        vh.tv_time1.setVisibility(View.VISIBLE);
                    } else if (pi.getActivity_status().equals("3")) {
                        vh.iv_used.setVisibility(View.VISIBLE);
//						vh.rl_time.setVisibility(View.GONE);
                        vh.tv_time1.setVisibility(View.GONE);
                    } else {
//						vh.rl_time.setVisibility(View.GONE);
                        vh.iv_used.setVisibility(View.GONE);
                        vh.tv_time1.setVisibility(View.GONE);
                    }

                    //小红点
                    vh.iv_newmsg.setVisibility(View.GONE);
                    if (msgType != null) {
                        List<MsgNew> msgNews = msgType.getMessages();
                        for (MsgNew msgNew : msgNews) {
                            if (!msgNew.isDirty() && msgNew.getMsg().equals(pi.getId())) {
                                vh.iv_newmsg.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    }

                    String urlKey;
                    if (screenWidth < 480) {
                        urlKey = pi.getImage_1000();
                    } else if (screenWidth >= 480 && screenWidth < 700) {
                        urlKey = pi.getImage_38();
                    } else {
                        urlKey = pi.getImage_100();
                    }

                    Glide.with(mContext).load("http:"+urlKey).placeholder(R.drawable.banner_default).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(vh.iv_activity);

                    convertView.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (msgType != null) {
                                List<MsgNew> msgNews = msgType.getMessages();
                                for (MsgNew msgNew : msgNews) {
                                    if (!msgNew.isDirty() && msgNew.getMsg().equals(pi.getId())) {
                                        vh.iv_newmsg.setVisibility(View.GONE);
                                        msgNew.setDirty(true);
                                        MessageLocalData data = msgData;
                                        MsgUtil.setObj(getActivity(), MsgUtil.INVEST, data);
                                        Intent in = new Intent();
                                        in.setAction(MsgUtil.MSG_ACTION_REFRESH);
                                        in.putExtra("flag", 0);
                                        getActivity().sendBroadcast(in);
                                    }
                                }
                            }
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), MainWebActivity.class);
                            intent.putExtra("title", pi.getName());
                            intent.putExtra("web_url", pi.getMobile_url());
                            startActivity(intent);
                            // MemorySave.MS.args = args;

                        }
                    });
                } else {
                    View view = View.inflate(getActivity(), R.layout.loading_item, null);
                    mP++;
                    loadData();
                    return view;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        public class ViewHolders {
            TextView tv_time1;
            TextView tv_time;
            RelativeLayout iv_used;
            RelativeLayout rl_time;
            ImageView iv_activity;
            ImageView iv_newmsg;

        }
    }

    @Override
    public void onRefresh() {
        reLoadDataDown();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    public static class TVIDTime {
        long diffTime = 0;
        TextView tv;
        String prjid;
        FinaceInfoMovementData pi;
        boolean isRepay = false;
    }

    @Override
    public void gainHotMovementSuccess(HotInfoMovementJson response) {
        try {
            if (response != null) {
                if (response.getBoolen().equals("1")) {
                    if (isDown) {
                        isDown = false;
                        mAdapter.clear();
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                    }

                    List<HotInfoMovementData> data = response.getData();
                    if (null != data) {
                        int size = data.size();
                        HotInfoMovementData item = null;
                        hasMore = size >= mPagesize;
                        mAdapter.mfilelist.addAll(data);
                    }

                } else {
                    hasMore = false;
                    if (response.getBoolen().trim().equals("0")) {
                        GoLoginUtil.BaseToLoginActivity(getActivity());
                    }
                    ToastUtil.showToast(getActivity(), response.getMessage());
                }
                if (mAdapter.mfilelist.size() <= 0
                        && !(response.getBoolen() != null && response.getBoolen().equals("0"))) {
                    mContent.findViewById(R.id.listView).setVisibility(View.GONE);
                    mContent.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                } else {
                    mContent.findViewById(R.id.rl_empty).setVisibility(View.GONE);
                    mContent.findViewById(R.id.listView).setVisibility(View.VISIBLE);
                }
            } else {
                hasMore = false;
            }
        } catch (Exception e) {
            hasMore = false;
            e.printStackTrace();
        } finally {
            mProgressBar.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            isRundata = false;
        }
    }

    @Override
    public void gainHotMovementFail() {
        ToastUtil.showToast(FinaceMovementChildFragment.this.getActivity(), "网络不给力!");
        mProgressBar.setVisibility(View.GONE);
        isRundata = false;
    }
}