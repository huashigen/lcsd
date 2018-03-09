package com.lcshidai.lc.ui.fragment.finance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.GetProjectBaseInfoImpl;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoBaseDescribeItem;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoBorrowerChildVInItem;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoBorrowerVInItem;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoData;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoExtensionItem;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoExtensionVInItem;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoFundInfoChildVInItem;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoFundInfoVInItem;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoJson;
import com.lcshidai.lc.model.finance.FinanceItemBaseInfoMaterialItem;
import com.lcshidai.lc.service.finance.GetFinanceProjectBaseInfoService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectBorrowerActivity;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.finance.MaterialActivity;
import com.lcshidai.lc.ui.fragment.BaseExpandableListFragment;
import com.lcshidai.lc.utils.Constants;
import com.lcshidai.lc.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目信息
 */
public class BaseInfoFragment extends BaseExpandableListFragment implements GetProjectBaseInfoImpl {

    private String mPrjId;
    private String mPrjType;
    private int isCollection = 0;

    private int mHeight = 0;

    private ItemAdapter mAdapter;
    public View mStateView;
    public String the_status;
    private String baoli_tips, prj_attribute_tips;
    public static final String URL_ = "__URL__:";
    public static final String SPRIT_STR = "#@#";
    public static final String SPRIT_IMG_STR = "_HOIMGOK_";
    private GetFinanceProjectBaseInfoService hfibis;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPrjId = args.getString("prj_id");
            mPrjType = args.getString(Constants.Project.PROJECT_TYPE);
            isCollection = args.getInt(Constants.Project.IS_COLLECTION);
        }
        hfibis = new GetFinanceProjectBaseInfoService((TRJActivity) getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragment_finance_baseinfo, container, false);
        mListView = (ExpandableListView) contentView.findViewById(R.id.listview);
        mListView.setGroupIndicator(null);

        mEmptyView = contentView.findViewById(R.id.empty);
        mListView.setEmptyView(mEmptyView);
        mProgressContainer = contentView.findViewById(R.id.progressContainer);
        mStateView = contentView.findViewById(R.id.iv_state);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStateView.setVisibility(View.GONE);

    }

    public boolean isFirst = false;
    public boolean baseIsFrist = true;

    @Override
    public void onResume() {
        super.onResume();
        isFirst = true;
        baseIsFrist = true;
        if (mHeight != 0) {
            Message msg = new Message();
            msg.what = 0;
            msg.arg1 = mHeight;
            mHandler.sendMessage(msg);
        }
        loadData();
    }

    List<String> mNameList = new ArrayList<String>();
    private String baseDscrK = "";

    void setObjectView(FinanceItemBaseInfoData bo) {
        mAdapter = new ItemAdapter();
        ArrayList<String> eparraylist = new ArrayList<String>();
        for (FinanceItemBaseInfoExtensionVInItem it : bo.baseInfoList) {
            eparraylist.add(it.getK() + ": " + it.getV());
        }
        if (eparraylist.size() > 0)
            mAdapter.add("项目基本信息", eparraylist);
        FinanceItemBaseInfoBaseDescribeItem baseDscr = bo.getBase_dscr();
        if (null != baseDscr) {
            ArrayList<String> decr = new ArrayList<String>();
            decr.add(baseDscr.getK() + ": " + baseDscr.getV());
            mAdapter.add(baseDscr.getK(), decr);
            baseDscrK = baseDscr.getK();
        }

        ArrayList<String> borrowerList = null;
        if (bo.borrowerInfoList != null && bo.borrowerInfoList.size() > 0) {
            for (FinanceItemBaseInfoBorrowerVInItem it : bo.borrowerInfoList) {
                borrowerList = new ArrayList<String>();
                if (it.getK() != null) {
                    borrowerList.add(it.getK());
                }
                if (it.getV() != null && it.getV().size() > 0) {
                    for (FinanceItemBaseInfoBorrowerChildVInItem item : it.getV()) {
                        borrowerList.add(item.getK() + ": " + item.getV());
                    }
                }
                mAdapter.add(it.getK(), borrowerList);
            }
        }
        ArrayList<String> fundInfoList = null;
        if (bo.getFundInfoList() != null && bo.getFundInfoList().size() > 0) {
            for (FinanceItemBaseInfoFundInfoVInItem it : bo.getFundInfoList()) {
                fundInfoList = new ArrayList<String>();
                if (it.getK() != null) {
                    fundInfoList.add(it.getK());
                }
                if (it.getV() != null && it.getV().size() > 0) {
                    for (FinanceItemBaseInfoFundInfoChildVInItem item : it.getV()) {
                        fundInfoList.add(item.getK() + ": " + item.getV());
                    }
                }
                mAdapter.add(it.getK(), fundInfoList);
            }
        }
        baoli_tips = bo.getBaoli_tips();
        ArrayList<String> exArrayList = null;
        if (bo.extensionList != null && bo.extensionList.size() > 0) {
            for (FinanceItemBaseInfoExtensionItem it : bo.extensionList) {
                exArrayList = new ArrayList<String>();
                if (it.getV() != null) {
                    exArrayList.add(it.getV());
                } else if (it.getvList() != null && it.getvList().size() > 0) {
                    for (FinanceItemBaseInfoExtensionVInItem item : it.getvList()) {
                        if (item.getV().indexOf(SPRIT_STR) != -1) {
                            exArrayList.add(item.getK() + SPRIT_STR + item.getV());
                        } else {
                            exArrayList.add(item.getK() + ": " + item.getV());
                        }
                    }
                }
                mAdapter.add(it.getK(), exArrayList);
            }
        }
        ArrayList<String> exArrayLists = null;
        if (bo.cailiaoImgList != null && bo.cailiaoImgList.size() > 0) {
            exArrayLists = new ArrayList<String>();
            bo.getCailiaoList();
            for (FinanceItemBaseInfoExtensionVInItem its : bo.cailiaoImgList) {
                if (its.getV() != null) {
                    exArrayLists.add(its.getV());
                }
            }
            for (FinanceItemBaseInfoMaterialItem item : bo.getCailiaoList()) {
                if (!TextUtils.isEmpty(item.getName())) {
                    mNameList.add(item.getName());
                }
            }

            mAdapter.add("公示材料", exArrayLists);
        }
        setAdapter(mAdapter);
        mListView.expandGroup(0);
        mListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1, int groupPosition,
                                        long groupId) {
                if (groupPosition == 0) {
                    return true;
                } else if (mPrjType.toUpperCase().equals("C10") && groupPosition == 2) {//C10借款人数量大于1
                    Intent intent = new Intent(getActivity(), FinanceProjectBorrowerActivity.class);
                    intent.putExtra(Constants.Project.PROJECT_ID, mPrjId);
                    intent.putExtra(Constants.Project.PROJECT_TYPE, mPrjType);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public void loadData() {
        hfibis.getProBaseInfo(mPrjId, isCollection);

    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, -(msg.arg1), 0, 0);
                    mListView.setLayoutParams(lp);
                    isFirst = false;

                    break;
                case 1:
//				if(baseIsFrist){
//					int position = msg.arg1;
//					mListView.expandGroup(position);
//				}
                    break;
                default:
                    break;
            }
        }
    };

    public View convert;

    public class ItemAdapter extends ExpandableListAdapter<String, String> {

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder groupHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_exlist_group_new_item, null);
                groupHolder = new GroupHolder();
                groupHolder.group_item_layout = (LinearLayout) convertView.findViewById(R.id.group_item_layout);
                groupHolder.img = (ImageView) convertView.findViewById(R.id.tag_img);
                groupHolder.title = (TextView) convertView.findViewById(R.id.title_view);
                groupHolder.ivPoint = (ImageView) convertView.findViewById(R.id.iv_ex_point);
                convertView.setTag(groupHolder);
            }
            groupHolder = (GroupHolder) convertView.getTag();
            if (isExpanded) {
                groupHolder.img.setImageResource(R.drawable.icon_indicator_up);
                groupHolder.ivPoint.setImageResource(R.drawable.exp_point_blue_bg);
            } else {
                groupHolder.img.setImageResource(R.drawable.icon_indicator_down);
                groupHolder.ivPoint.setImageResource(R.drawable.exp_point_gray_bg);
            }
            if (groupPosition == 0) {
                groupHolder.img.setVisibility(View.GONE);
            } else {
                groupHolder.img.setVisibility(View.VISIBLE);
            }
            if (mPrjType.toUpperCase().equals("C10") && groupPosition == 2) {
                groupHolder.img.setRotation(-90);
            } else {
                groupHolder.img.setRotation(0);
            }
            groupHolder.title.setText(getGroup(groupPosition));
            if (baseIsFrist) {
                if (getGroup(groupPosition).equals(baseDscrK) && !isExpanded) {
                    mListView.expandGroup(groupPosition);
                    baseIsFrist = false;
                }
            }
            convert = convertView;
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            try {
                if (isFirst) {
                    mHeight = convert.getHeight();
                    Message msg = new Message();
                    msg.what = 0;
                    msg.arg1 = mHeight;
                    mHandler.sendMessage(msg);
                }
                final String str;
                str = getChild(groupPosition, childPosition);
                if (str.indexOf(SPRIT_STR) != -1) {
                    TextView tv1, tv2, tv3;
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_exlist_ch_thi, null);
                    tv1 = (TextView) convertView.findViewById(R.id.tv_text1);
                    tv2 = (TextView) convertView.findViewById(R.id.tv_text2);
                    tv3 = (TextView) convertView.findViewById(R.id.tv_text3);
                    String[] stra = str.split(SPRIT_STR);
                    if (stra.length > 2) {
                        tv1.setText(stra[0]);
                        tv2.setText(stra[1]);
                        tv3.setText(stra[2]);
                    }

                    if (childPosition == 0) {
                        tv1.setTextColor(Color.rgb(0x46, 0x46, 0x46));// #464646
                        tv2.setTextColor(Color.rgb(0x46, 0x46, 0x46));// #464646
                        tv3.setTextColor(Color.rgb(0x46, 0x46, 0x46));// #464646
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    } else {
                        tv1.setTextColor(Color.rgb(0x88, 0x88, 0x88));// ##888888
                        tv2.setTextColor(Color.rgb(0x88, 0x88, 0x88));// ##888888
                        tv3.setTextColor(Color.rgb(0x88, 0x88, 0x88));// ##888888
                        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    }
                } else if (str.indexOf(SPRIT_IMG_STR) != -1) {
                    ImageView iv1, iv2, iv3;
                    TextView tv1, tv2;
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_exlist_ch_img, parent, false);
                    iv1 = (ImageView) convertView.findViewById(R.id.iv1);
                    iv2 = (ImageView) convertView.findViewById(R.id.iv2);
                    // iv3 = (ImageView) convertView.findViewById(R.id.iv3);
                    tv1 = (TextView) convertView.findViewById(R.id.tv1);
                    tv2 = (TextView) convertView.findViewById(R.id.tv2);
                    String[] sarray = str.split(SPRIT_IMG_STR);
                    int index = 0;
                    for (String string : sarray) {
                        if (index == 0) {
                            Glide.with(mContext).load("http:" + string).into(iv1);
                            tv1.setText(mNameList.get(childPosition * 2 + index));
                            iv1.setOnClickListener(new MyOnclickListener(childPosition * 2 + index));
                        } else if (index == 1) {
                            Glide.with(mContext).load("http:" + string).into(iv2);
                            tv2.setText(mNameList.get(childPosition * 2 + index));
                            iv2.setOnClickListener(new MyOnclickListener(childPosition * 2 + index));
                        }
                        index++;
                    }
                } else {
                    TextView tv_key, tv_value, tv_prj_attribute_tips, tv_key1, tv_value1, tv_base_dscr;
                    LinearLayout ll_prj_attribute_tips, ll_extension;
                    TextView tv;
                    TextView tv1;
                    RelativeLayout rl_key_value;
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_exlist_child_item, null);
                    tv_key = (TextView) convertView.findViewById(R.id.tv_key);
                    tv_value = (TextView) convertView.findViewById(R.id.tv_value);
                    tv_key1 = (TextView) convertView.findViewById(R.id.tv_key1);
                    tv_value1 = (TextView) convertView.findViewById(R.id.tv_value1);
                    ImageView im_doubt = (ImageView) convertView.findViewById(R.id.doubt);
                    final TextView tv_confident = (TextView) convertView.findViewById(R.id.confident);
                    im_doubt.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (!(tv_confident.getVisibility() == View.VISIBLE)) {
                                tv_confident.setVisibility(View.VISIBLE);
                                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.text_show);
                                tv_confident.startAnimation(animation);
                            }
                        }
                    });

                    tv_prj_attribute_tips = (TextView) convertView.findViewById(R.id.tv_prj_attribute_tips);
                    ll_prj_attribute_tips = (LinearLayout) convertView.findViewById(R.id.ll_prj_attribute_tips);
                    ll_extension = (LinearLayout) convertView.findViewById(R.id.ll_extension);
                    rl_key_value = (RelativeLayout) convertView.findViewById(R.id.rl_key_value);
                    tv_base_dscr = (TextView) convertView.findViewById(R.id.tv_base_dscr);
                    tv = (TextView) convertView.findViewById(R.id.tv_text1);
                    tv1 = (TextView) convertView.findViewById(R.id.tv_text2);
                    if (str.indexOf(URL_) != -1) {
                        tv.setTextColor(Color.BLUE);// #464646
                        // tv.setText("查看原项目");
                        tv.setText(Html.fromHtml("<u>查看原项目</u>"));
                        tv.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(mContext, FinanceProjectDetailActivity.class);
                                intent.putExtra("prj_id", str.trim().replace(URL_, ""));
                                mContext.startActivity(intent);
                                // getActivity().finish();
                            }
                        });
                    } else {
                        // tv.setText(str);
                        int index = str.indexOf(":");
                        if (index != -1) {
                            String key = str.substring(0, index + 2);
                            String value = str.substring(index + 2, str.length());
                            if (key.contains("资金用途") || key.contains("回款来源")
                                    || key.contains("项目投向") || key.contains("投资目标")) {
                                rl_key_value.setVisibility(View.GONE);
                                tv_base_dscr.setVisibility(View.GONE);
                                ll_extension.setVisibility(View.VISIBLE);
                                tv_key1.setText(key);
                                tv_value1.setText(value);
                            } else if ((!StringUtils.isEmpty(baseDscrK)) && key.contains(baseDscrK)) {
                                rl_key_value.setVisibility(View.GONE);
                                ll_extension.setVisibility(View.GONE);
                                tv_base_dscr.setVisibility(View.VISIBLE);
                                value = value.replace("<br/>", "\n").replace("<br>", "\n")
                                        .replace("<br />", "\n").replace("<br />", "\n");
                                tv_base_dscr.setText(value);

                            } else {
                                ll_extension.setVisibility(View.GONE);
                                tv_base_dscr.setVisibility(View.GONE);
                                rl_key_value.setVisibility(View.VISIBLE);
//								if(key.contains(""))
                                tv_key.setText(key);
                                if (value.contains("元") || value.contains("万") || value.contains("亿")) {
                                    tv_value.setTextColor(Color.RED);
                                } else {
                                    tv_value.setTextColor(getResources().getColor(R.color.text_color));
                                }
                                tv_value.setText(value);
                            }
                        }
                        if (str.contains("剩余时间")) {
                            if (mTimeCount != null) {
                                mTimeCount.cancel();
                            }
                            // Long.parseLong(pi.end_bid_time_diff);
                            Long remaning_time;
                            remaning_time = Long.parseLong(str.substring(5, str.length()).trim());
                            if (remaning_time >= 0) {

                                mTimeCount = new TimeCount(Math.abs(remaning_time) * 1000 + 1000,
                                        1000, str.substring(5, str.length()).trim(), str, tv);
                                mTimeCount.start();
                            }
                        }

                        if (isLastChild && groupPosition == 0) {
                            ll_prj_attribute_tips.setVisibility(View.VISIBLE);
                            tv_prj_attribute_tips.setText(prj_attribute_tips);

                            if (baoli_tips != null && !("").equals(baoli_tips)) {
                                tv1.setVisibility(View.VISIBLE);
                                tv1.setText(baoli_tips);
                            } else {
                                tv1.setVisibility(View.GONE);
                            }
                        } else {
                            ll_prj_attribute_tips.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (Exception e) {
            }
            return convertView;
        }

    }

    class MyOnclickListener implements View.OnClickListener {

        int index = 0;

        public MyOnclickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MaterialActivity.class);
            intent.putExtra("prj_id", mPrjId);
            intent.putExtra("index", index);
            startActivity(intent);
        }

    }

    class GroupHolder {
        LinearLayout group_item_layout;
        ImageView ivPoint;
        ImageView img;
        TextView title;
    }

    @Override
    public void getProjectBaseInfoSuccess(FinanceItemBaseInfoJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    FinanceItemBaseInfoData jobj = response.getData();
                    List<FinanceItemBaseInfoMaterialItem> cailiaoArray = jobj.getCailiaoList();
                    String tempimgv = "";
                    FinanceItemBaseInfoExtensionVInItem okv = null;
                    if (cailiaoArray != null)
                        for (int index = 0; index < cailiaoArray.size(); index++) {
                            if (index % 2 == 0) {
                                tempimgv = "";
                                okv = new FinanceItemBaseInfoExtensionVInItem();
                            }
                            tempimgv += cailiaoArray.get(index).getBig() + SPRIT_IMG_STR;
                            if (index % 2 == 1 || (index % 2 != 1 && index == cailiaoArray.size() - 1)) {
                                okv.setV(tempimgv);
                                jobj.cailiaoImgList.add(okv);
                            }
                        }
                    prj_attribute_tips = jobj.getPrj_attribute_tips();
                    setObjectView(jobj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mProgressContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void getProjectBaseInfoFail() {
        mProgressContainer.setVisibility(View.GONE);
    }

    public class TimeCount extends CountDownTimer {

        private String mBidState;

        TextView mtv;

        boolean isFinish = false;
        private String strz;

        public TimeCount(long millisInFuture, long countDownInterval, String bidState, String str, TextView tv) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            mBidState = bidState;
            strz = str;
            mtv = tv;
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            try {
                if (!isFinish) {
                    if (strz.contains("剩余时间")) {
                        mtv.setText("剩余时间:—:—:--");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            try {
                StringBuffer stringBuffer = new StringBuffer();
                long second = millisUntilFinished / 1000;
                long s = second % 60; // 秒
                long mi = (second - s) / 60 % 60; // 分钟
                long h = ((second - s) / 60 - mi) / 60 % 24; // 小时
                long d = (((second - s) / 60 - mi) / 60 - h) / 24; // 天
                if (d > 0) {
                    stringBuffer.append(d + "天");
                }
                stringBuffer.append(h + "时");
                stringBuffer.append(mi + "分");
                stringBuffer.append(s + "秒");
                if (strz.contains("剩余时间")) {
                    mtv.setText("剩余时间:" + stringBuffer);
                }
            } catch (Exception e) {
                isFinish = true;
            }
        }
    }

    TimeCount mTimeCount;

    @Override
    public void onDestroy() {
        if (mTimeCount != null) {
            mTimeCount.isFinish = true;
            mTimeCount.cancel();
        }
        super.onDestroy();
    }
}