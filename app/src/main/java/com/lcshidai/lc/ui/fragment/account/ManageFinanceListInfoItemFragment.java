package com.lcshidai.lc.ui.fragment.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.PIPWCallBack;
import com.lcshidai.lc.impl.PrjCountImpl;
import com.lcshidai.lc.impl.account.CancelCashImpl;
import com.lcshidai.lc.impl.account.ManageFinanceListInfoItemImpl;
import com.lcshidai.lc.impl.transfer.CashCheckAddImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.account.ManageFinanceListInfo;
import com.lcshidai.lc.model.account.ManageFinanceListInfoItem;
import com.lcshidai.lc.model.account.ManageFinanceListInfoItemJson;
import com.lcshidai.lc.model.finance.reward.FinanceInvestPBuyCheckData;
import com.lcshidai.lc.model.transfer.CashCheckAddData;
import com.lcshidai.lc.model.transfer.CashCheckAddJson;
import com.lcshidai.lc.service.account.HttpCancelCashService;
import com.lcshidai.lc.service.account.HttpManageFinanceListInfoItemService;
import com.lcshidai.lc.service.transfer.HttpCashCheckAddService;
import com.lcshidai.lc.ui.AgreementActivity;
import com.lcshidai.lc.ui.account.ManageFinanceListProtocol;
import com.lcshidai.lc.ui.account.RepaymentListActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.FinanceProjectDetailActivity;
import com.lcshidai.lc.ui.transfer.CashActivity;
import com.lcshidai.lc.utils.GoClassUtil;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.TestLogin;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.ppwindow.PayPasswordPopupWindow;
import com.lcshidai.lc.widget.quickaction.QuickAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 投资记录 变现记录
 *
 * @author 000814
 */
public class ManageFinanceListInfoItemFragment extends TRJFragment implements ManageFinanceListInfoItemImpl, PIPWCallBack, CancelCashImpl, CashCheckAddImpl {
    HttpManageFinanceListInfoItemService hmfliis;
    HttpCashCheckAddService hccas;
    HttpCancelCashService hcccs;
    public PrjCountImpl prjCi;

    View contextView;
    ListView mListView;
    View mProgressBar;

    private int mPagesize = 3;
    private int mP = 1;
    boolean hasMore;

    public String mType = "0";
    public String mStatus = "0";
    public String mCurrentPType = "0";
    public boolean isTransfer;
    PayPasswordPopupWindow pw;
    TextView back_time, make_time;
    public String paysc = null;
    public String delsc = null;
    public boolean isRepay = false;
    public boolean isPay = true;
    ImageView parr, darr;

    public boolean isDel = true;

    //	private String prj_order_id;
//	private String prjName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hccas = new HttpCashCheckAddService((TRJActivity) getActivity(), this);
        hcccs = new HttpCancelCashService((TRJActivity) getActivity(), this);
        hmfliis = new HttpManageFinanceListInfoItemService((TRJActivity) getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.fragment_account_invest_records_info, container, false);
        mListView = (ListView) contextView.findViewById(R.id.listView);
        mProgressBar = contextView.findViewById(R.id.progressContainer);
        pw = new PayPasswordPopupWindow((TRJActivity) getActivity(), this);
        if (isTransfer) {
            mTransferAdapter = new ItemTransferAdapter(getActivity());
            mListView.setAdapter(mTransferAdapter);
        } else {
            mAdapter = new ItemAdapter(getActivity());
            mListView.setAdapter(mAdapter);
        }
        qa = new QuickAction(getActivity());
        qa.dismissView();
        back_time = (TextView) contextView.findViewById(R.id.back_time);
        make_time = (TextView) contextView.findViewById(R.id.make_time);

        back_time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (isPay) {
                    paysc = "desc";
                    delsc = "asc";
                    isPay = false;
                    parr.setBackground(getResources().getDrawable(R.drawable.img_arrow_bottom));
                    hmfliis.gainManageFinanceListInfoItem(mCurrentPType, mType,
                            1, mPagesize, isTransfer, mStatus, paysc, delsc);

                } else {
                    paysc = "asc";
                    delsc = "desc";
                    isPay = true;
                    parr.setBackground(getResources().getDrawable(R.drawable.img_arrow_top));
                    hmfliis.gainManageFinanceListInfoItem(mCurrentPType, mType,
                            1, mPagesize, isTransfer, mStatus, paysc, delsc);
                }

            }
        });

        make_time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (isDel) {
                    paysc = "asc";
                    delsc = "desc";
                    isDel = false;
                    darr.setBackground(getResources().getDrawable(R.drawable.img_arrow_bottom));
                    hmfliis.gainManageFinanceListInfoItem(mCurrentPType, mType,
                            1, mPagesize, isTransfer, mStatus, paysc, delsc);
                } else {
                    paysc = "desc";
                    delsc = "asc";
                    isDel = true;
                    darr.setBackground(getResources().getDrawable(R.drawable.img_arrow_top));
                    hmfliis.gainManageFinanceListInfoItem(mCurrentPType, mType,
                            1, mPagesize, isTransfer, mStatus, paysc, delsc);
                }
            }
        });
        return contextView;
    }

    public ItemAdapter mAdapter;
    public ItemTransferAdapter mTransferAdapter;

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        if (MemorySave.MS.isCashSuccessBack) {
            MemorySave.MS.isCashSuccessBack = false;
            prjCi.prjCount();
        }
    }

    public void loadData() {
        hasMore = false;
        mListView.setVisibility(View.GONE);
        if (isTransfer) {
            mTransferAdapter.clear();
            mTransferAdapter.notifyDataSetChanged();
        } else {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
        }
        mP = 1;
        mProgressBar.setVisibility(View.VISIBLE);
        loadMore();
    }


    private void loadMore() {
        hmfliis.gainManageFinanceListInfoItem("", mType, mP++, mPagesize, isTransfer, mStatus, paysc, delsc);
    }

    QuickAction qa;

    public class ItemAdapter extends ArrayAdapter<ManageFinanceListInfoItem> {
        private Context mContext;
        public List<ManageFinanceListInfoItem> mfilelist = new ArrayList<ManageFinanceListInfoItem>();
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
        public void add(ManageFinanceListInfoItem jobj) {
            mfilelist.add(jobj);
        }

        @Override
        public void addAll(
                Collection<? extends ManageFinanceListInfoItem> collection) {
            mfilelist.addAll(collection);
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
        public ManageFinanceListInfoItem getItem(int position) {
            return mfilelist.get(position);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolders vh;
            if (position < size) {
//				if(convertView == null|| convertView.getTag() == null
//						|| !(convertView.getTag() instanceof ViewHolders)){
                vh = new ViewHolders();
                if (isRepay) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.account_invest_record_item_repay, null);
                    vh.tv_top_content_right = (TextView) convertView.findViewById(R.id.tv_top_content_right);
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.account_invest_records_item, null);
                }
                vh.tv_prj_type = (TextView) convertView.findViewById(R.id.tv_prj_type);
                vh.tv_prj_name = (TextView) convertView.findViewById(R.id.tv_prj_name);
                vh.repay_status = (TextView) convertView.findViewById(R.id.repay_status);
                vh.im_transfer = (ImageView) convertView.findViewById(R.id.im_transfer);
                vh.im_transfer_cn = (ImageView) convertView.findViewById(R.id.im_transfer_cn);
                vh.im_new = (ImageView) convertView.findViewById(R.id.im_new);
                vh.im_bianxian = (ImageView) convertView.findViewById(R.id.im_can_duixian);
                vh.iv_activity = (ImageView) convertView.findViewById(R.id.iv_activity);
                vh.iv_limit = (ImageView) convertView.findViewById(R.id.iv_limit);
                vh.iv_phone_spec = (ImageView) convertView.findViewById(R.id.iv_phone_spec);
                vh.content_tv_1 = (TextView) convertView.findViewById(R.id.content_tv_1);
                vh.content_tv_2 = (TextView) convertView.findViewById(R.id.content_tv_2);
                vh.content_tv_3 = (TextView) convertView.findViewById(R.id.content_tv_3);
                vh.content_tv_7 = (TextView) convertView.findViewById(R.id.content_tv_7);
                vh.content_tv_8 = (TextView) convertView.findViewById(R.id.content_tv_8);
                vh.tv_date_next_repayment = (TextView) convertView.findViewById(R.id.tv_date_next_repayment);
                vh.btn_view_arg = (Button) convertView.findViewById(R.id.btn_view_arg);
                vh.btn_repay_list = (Button) convertView.findViewById(R.id.btn_repay_list);
                vh.btn_transfer = (Button) convertView.findViewById(R.id.btn_transfer);
                vh.tv_nv = (TextView) convertView.findViewById(R.id.tv_nv);
                vh.title_tv_2 = (TextView) convertView.findViewById(R.id.title_tv_2);
                vh.title_tv_2_img = (ImageView) convertView.findViewById(R.id.title_tv_2_img);
                vh.title_tv_1 = (TextView) convertView.findViewById(R.id.title_tv_1);
                vh.ll_content = convertView.findViewById(R.id.ll_topc);
                vh.tv_content = (TextView) convertView.findViewById(R.id.tv_top_content);
                vh.iv_blue_flag = (ImageView) convertView.findViewById(R.id.iv_blue_flag);
                vh.title_tv_3 = (TextView) convertView.findViewById(R.id.title_tv_3);
                vh.pb = (ProgressBar) convertView.findViewById(R.id.pb);
                vh.iv_pre_sale = (ImageView) convertView.findViewById(R.id.iv_pre_sale);
                convertView.setTag(vh);
//				}else{
//					vh=(ViewHolders) convertView.getTag();	
//				}
                final ManageFinanceListInfoItem item = mfilelist.get(position);

				
				/*System.out.println("-----item.getPrj_type_display()-------"+item.getPrj_type_display());
                System.out.println("-----item.getPrj_business_type_name()-------"+item.getPrj_business_type_name());
				System.out.println("-----item.getPrj_name()-------"+item.getPrj_name());*/
                vh.tv_prj_type.setText(item.getPrj_type_display());
                if (isRepay) {
                    vh.tv_prj_name.setText(item.getPrj_name());
                } else {
                    if (item.getPrj_business_type_name() != null) {
                        vh.tv_prj_name.setText(item.getPrj_business_type_name() + item.getPrj_name());
                    } else {
                        vh.tv_prj_name.setText(item.getPrj_name());
                    }
                }

                if (isRepay) {
                    vh.repay_status.setVisibility(View.VISIBLE);
                    vh.repay_status.setText(item.getStatus_show());
                    vh.title_tv_1.setText("预售投资金额(元)");
                    vh.title_tv_2.setText("期限:");
                    vh.title_tv_3.setText("预期到期本息:");
                    vh.content_tv_1.setText(item.getMoney_show());
                    vh.content_tv_2.setText(item.getTime_limit_show());
                    vh.content_tv_3.setText(item.getProfit_show());
                    if (item.getBuy_time_show() != null) {
                        vh.tv_top_content_right.setText(item.getBuy_time_show());
                    }
                    vh.tv_content.setText(item.getCtime_show());
                    vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                    vh.btn_view_arg.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (item.getStatus().equals("3")) {
                                Intent intent = new Intent(getActivity(), AgreementActivity.class);
                                intent.putExtra("title", "服务协议");
                                intent.putExtra("url", item.getServer_protocol());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra("server_protocol", item.getServer_protocol());
                                intent.putExtra("pre_protocol", item.getPre_protocol());
                                intent.setClass(getActivity(), ManageFinanceListProtocol.class);
                                getActivity().startActivity(intent);
                            }
                        }
                    });
                } else {
                    if (item.getIs_have_contract().equals("1")) {
                        vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                        if (item.getIs_pre_sale().equals("1")) {
                            vh.iv_pre_sale.setVisibility(View.VISIBLE);
                            vh.btn_view_arg.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.putExtra("server_protocol", item.getServer_protocol());
                                    intent.putExtra("isOrderID", true);
                                    intent.putExtra("id", item.getOrder_id());
                                    intent.setClass(getActivity(), ManageFinanceListProtocol.class);
                                    getActivity().startActivity(intent);
                                }
                            });
                        } else {
                            vh.iv_pre_sale.setVisibility(View.INVISIBLE);
                            vh.btn_view_arg.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), AgreementActivity.class);
                                    intent.putExtra("title", "");
                                    intent.putExtra("id", item.getOrder_id());
                                    startActivity(intent);
                                }
                            });
                        }
                    } else {
                        if (item.getIs_pre_sale().equals("1")) {
                            vh.iv_pre_sale.setVisibility(View.VISIBLE);
                            vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                            vh.btn_view_arg.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), AgreementActivity.class);
                                    intent.putExtra("title", "服务协议");
                                    intent.putExtra("url", item.getServer_protocol());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            vh.iv_pre_sale.setVisibility(View.INVISIBLE);
                            vh.btn_view_arg.setOnClickListener(null);
                            vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_sub_gray));
                        }
                    }

                    vh.tv_content.setText(item.getDate_deal());
                    vh.content_tv_3.setText(item.getRepay_way_display());
                    vh.content_tv_2.setText(item.getIncoming());
                    vh.content_tv_1.setText(item.getMoney());
                    if (item.getReward_type() != null && item.getReward_type().equals("1") && item.getReward_type_tips() != null) {
                        vh.content_tv_7.setVisibility(View.VISIBLE);
                        vh.content_tv_7.setText("(" + item.getReward_type_tips() + ")");
                    } else if (item.getReward_type() != null && item.getReward_type().equals("2") && item.getReward_type_tips() != null) {
                        vh.content_tv_7.setVisibility(View.VISIBLE);
                        vh.content_tv_7.setText("(" + item.getReward_type_tips() + ")");
                    } else if (item.getReward_type() != null && item.getReward_type().equals("3") && item.getReward_type_tips() != null) {
                        vh.content_tv_8.setVisibility(View.VISIBLE);
                        vh.content_tv_8.setText("(" + item.getReward_type_tips() + ")");
                    } else {
                        vh.content_tv_7.setVisibility(View.GONE);
                        vh.content_tv_8.setVisibility(View.GONE);
                    }
                    vh.repay_status.setVisibility(View.GONE);
                    vh.title_tv_1.setText("投资金额(元)");
//					vh.title_tv_2.setText("期限:");
                    if (item.getIcon_activity() != null && item.getIcon_activity().equals("1")) {
                        vh.iv_activity.setVisibility(View.VISIBLE);
                        if (item.getExt() == null || item.getExt().getIcon() == null || item.getExt().getIcon().equals("")) {
                            vh.iv_activity.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_icon));
                        } else {
                            vh.iv_activity.setBackgroundDrawable(null);
                            Glide.with(mContext).load(item.getExt().getIcon()).into(vh.iv_activity);
                        }
                    } else {
                        vh.iv_activity.setVisibility(View.GONE);
                    }
                    vh.title_tv_2.setMovementMethod(LinkMovementMethod.getInstance());
                    vh.title_tv_2.setText("综合收益(元):");
                    vh.title_tv_2_img.setVisibility(View.VISIBLE);
                    vh.title_tv_2_img.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            qa.getContentView().setText("综合收益=本息收益(" + item.getBenxi() + ")\n+利率奖励(" + item.getRewardMoney() + ")");
                            qa.showTOP(vh.title_tv_2_img);
                        }
                    });
                    vh.title_tv_2.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            qa.getContentView().setText("综合收益=本息收益(" + item.getBenxi() + ")\n+利率奖励(" + item.getRewardMoney() + ")");
                            qa.showTOP(vh.title_tv_2_img);
                        }
                    });
                    vh.title_tv_3.setText("回款方式:");
                }

                if (item.getBid_status().equals("5")) {
                    vh.tv_nv.setText("最后回款日期:");
                } else {
                    vh.tv_nv.setText("下一个回款日期:");
                }

                vh.im_transfer.setVisibility(View.GONE);

                if (item.getCan_bianxian() != null && item.getCan_bianxian().equals("1")) {
                    vh.im_bianxian.setVisibility(View.VISIBLE);
                } else {
                    vh.im_bianxian.setVisibility(View.GONE);
                }


                vh.tv_date_next_repayment.setText(item.getDate_next_repayment());

                if ("1".equals(item.getIcon_bianxian())) {
                    vh.btn_transfer.setVisibility(View.VISIBLE);
                    vh.btn_transfer.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            cashCheck(item.getOrder_id(), "0", "0", item.getPrj_name());

                        }
                    });
                } else {
                    vh.btn_transfer.setVisibility(View.GONE);
                }

                if (item.getIs_have_repayplan() != null && item.getIs_have_repayplan().equals("1")) {
                    vh.btn_repay_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                    vh.btn_repay_list.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), RepaymentListActivity.class);
                            intent.putExtra("id", item.getOrder_id());
                            startActivity(intent);
                        }
                    });
                } else {
                    vh.btn_repay_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_sub_gray));
                    vh.btn_repay_list.setOnClickListener(null);
                }

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        if (isRepay) {
                            args.putString("prj_id", item.getId());
                        } else {
                            args.putString("prj_id", item.getPrj_id());
                        }
                        args.putString("canInvest", item.getCanInvest());
//						if(item.getIcon_transfer()!=null&&item.getIcon_transfer().equals("1"))args.putString("mTransferId", item.getTransfer_id());
//						getActivity().getIntent().putExtra("goClass", FinanceProjectDetailActivity.class.getName());
                        GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
                        GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
                        MemorySave.MS.args = args;
                        new TestLogin(getActivity()).testIt((TRJActivity) getActivity());
                    }
                });
            } else {
                View view = View.inflate(getActivity(), R.layout.loading_item, null);
                loadMore();
                // new ListViewUtility().setListViewHeightBasedOnChildren(mListView);
                return view;
            }
            return convertView;

        }

    }

    private SpannableString getClickSpan(OnClickListener listener, String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new MyURLSpan(listener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    private class MyURLSpan extends ClickableSpan {
        private OnClickListener listener;

        MyURLSpan(OnClickListener lis) {
            this.listener = lis;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#666666"));    //改变颜色
            ds.setUnderlineText(false);    //去除下划线
        }

        @Override
        public void onClick(View widget) {
            listener.onClick(widget);
        }
    }

    public class ItemTransferAdapter extends ArrayAdapter<ManageFinanceListInfoItem> {
        private Context mContext;
        public List<ManageFinanceListInfoItem> mfilelist = new ArrayList<ManageFinanceListInfoItem>();
        public int size;

        public ItemTransferAdapter(Activity context) {
            super(context, 0);
            mContext = context;
        }

        @Override
        public int getCount() {
            size = mfilelist.size();
            return size + (hasMore ? 1 : 0);
        }

        @Override
        public void add(ManageFinanceListInfoItem jobj) {
            mfilelist.add(jobj);
        }

        @Override
        public void addAll(
                Collection<? extends ManageFinanceListInfoItem> collection) {
            mfilelist.addAll(collection);
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
        public ManageFinanceListInfoItem getItem(int position) {
            return mfilelist.get(position);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolderps vh;
            if (position < size) {
//				if(convertView==null||convertView.getTag()==null){
                vh = new ViewHolderps();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.account_cash_item, null);
                vh.tv_prj_type = (TextView) convertView.findViewById(R.id.tv_prj_type);
                vh.tv_prj_name = (TextView) convertView.findViewById(R.id.tv_prj_name);
                vh.im_bianxian = (ImageView) convertView.findViewById(R.id.im_can_duixian);
                vh.im_transfer = (ImageView) convertView.findViewById(R.id.im_transfer);
                vh.im_transfer_cn = (ImageView) convertView.findViewById(R.id.im_transfer_cn);
                vh.im_new = (ImageView) convertView.findViewById(R.id.im_new);
                vh.iv_activity = (ImageView) convertView.findViewById(R.id.iv_activity);
                vh.iv_limit = (ImageView) convertView.findViewById(R.id.iv_limit);
                vh.content_tv_1 = (TextView) convertView.findViewById(R.id.content_tv_1);
                vh.content_tv_2 = (TextView) convertView.findViewById(R.id.content_tv_2);
                vh.content_tv_4 = (TextView) convertView.findViewById(R.id.content_tv_4);
                vh.content_tv_5 = (TextView) convertView.findViewById(R.id.content_tv_5);

                vh.iv_phone_spec = (ImageView) convertView.findViewById(R.id.iv_phone_spec);
                vh.tv_date_next_repayment = (TextView) convertView.findViewById(R.id.tv_date_next_repayment);
                vh.btn_view_arg = (Button) convertView.findViewById(R.id.btn_view_arg);
                vh.btn_repay_list = (Button) convertView.findViewById(R.id.btn_repay_list);
                vh.btn_transfer = (Button) convertView.findViewById(R.id.btn_transfer);
                vh.ll_nextview = convertView.findViewById(R.id.ll_nextview);

                vh.ll_content = convertView.findViewById(R.id.ll_topc);
                vh.tv_content = (TextView) convertView.findViewById(R.id.tv_top_content);
                vh.iv_blue_flag = (ImageView) convertView.findViewById(R.id.iv_blue_flag);
                vh.tv_top_1 = (TextView) convertView.findViewById(R.id.tv_top_1);

                vh.title_tv_1 = (TextView) convertView.findViewById(R.id.title_tv_1);
                vh.title_tv_2 = (TextView) convertView.findViewById(R.id.title_tv_2);
                vh.title_tv_4 = (TextView) convertView.findViewById(R.id.title_tv_4);
                vh.title_tv_5 = (TextView) convertView.findViewById(R.id.title_tv_5);
                vh.pb = (ProgressBar) convertView.findViewById(R.id.pb);
                convertView.setTag(vh);
//				}else{
//					vh=(ViewHolderps) convertView.getTag();	
//				}
                final ManageFinanceListInfoItem item = mfilelist.get(position);

                if (item.getType().equals("1") || item.getType().equals("3")) {
                    vh.tv_top_1.setText("成交日期:");
                } else {
                    vh.tv_top_1.setText("发布日期:");
                }
                vh.tv_content.setText(item.getOrder_mtime());

                vh.content_tv_1.setText(item.getRate());
                vh.content_tv_2.setText(item.getCurr_time_limit());

                if (item.getType().equals("1")) {

                    vh.title_tv_4.setText("可变现次数");
                    vh.title_tv_5.setText("可变现额度(元)");

                    vh.content_tv_4.setText(item.getLeft_cash_times());
                    vh.content_tv_5.setText(item.getCan_money());
//					vh.content_tv_6.setVisibility(View.GONE);
//					vh.title_tv_6.setVisibility(View.GONE);
                } else {

                    if (item.getType().equals("3")) {
                        vh.title_tv_4.setText("实际变现金额(元)");
                        vh.title_tv_5.setText("收益额度");
                        vh.content_tv_4.setText(item.getReal_cash_money());
                        vh.content_tv_5.setText(item.getIncome());
                    } else {
                        vh.title_tv_4.setText("变现金额(元)");
                        vh.title_tv_5.setText("实际变现金额(元)");
                        vh.content_tv_4.setText(item.getPlan_cash_money());
                        vh.content_tv_5.setText(item.getReal_cash_money());
                    }
                }

                if (!StringUtils.isInteger(item.getSchedule())) item.getSchedule().equals("0");
                vh.pb.setProgress(Integer.parseInt(item.getSchedule()));
                vh.tv_date_next_repayment.setText(item.getExpect_repay_time());
                vh.tv_prj_name.setText(item.getPrj_name());

                if (item.getIcon_activity() != null && item.getIcon_activity().equals("1")) {
                    vh.iv_activity.setVisibility(View.VISIBLE);
                    if (item.getExt() == null || item.getExt().getIcon() == null || item.getExt().getIcon().equals("")) {
                        vh.iv_activity.setBackgroundDrawable(getResources().getDrawable(R.drawable.activity_icon));
                    } else {
                        vh.iv_activity.setBackgroundDrawable(null);
                        Glide.with(mContext).load(item.getExt().getIcon()).into(vh.iv_activity);
                    }
                } else {
                    vh.iv_activity.setVisibility(View.GONE);
                }

                if (!item.getIs_view().equals("0")) {
                    vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                    vh.btn_view_arg.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (item.getProtcol_url() != null) {
                                try {
                                    ManageFinanceListInfo jObj = item.getProtcol_url();
                                    String fun = ((TRJActivity) getActivity()).resolveString(jObj.getFun(), "remaining_amount", "", "");
                                    String name = ((TRJActivity) getActivity()).resolveString(jObj.getName(), "remaining_amount", "", "");
                                    String type = ((TRJActivity) getActivity()).resolveString(jObj.getType(), "remaining_amount", "", "");
                                    String id = ((TRJActivity) getActivity()).resolveString(jObj.getId(), "remaining_amount", "", "");
                                    Intent intent = new Intent(getActivity(), AgreementActivity.class);
                                    intent.putExtra("intent_flag", 1);
                                    intent.putExtra("title", "");
                                    intent.putExtra("fun", fun);
                                    intent.putExtra("name", name);
                                    intent.putExtra("type", type);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                } else {
                    vh.btn_view_arg.setOnClickListener(null);
                    vh.btn_view_arg.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_cancel));
                }

                if (item.getType().equals("1")) {
                    vh.btn_transfer.setText("立即变现");
                    vh.btn_transfer.setVisibility(View.VISIBLE);
                    vh.btn_transfer.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            cashCheck(item.getPrj_order_id(), "0", "0", item.getPrj_name());
                        }
                    });
                } else if (item.getType().equals("2")) {
                    vh.btn_transfer.setText("取消变现");
                    vh.btn_transfer.setVisibility(View.VISIBLE);
                    vh.btn_transfer.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            cancelCheck(item.getFid());
                        }
                    });
                } else {
                    vh.btn_transfer.setVisibility(View.GONE);
                }

                if (item.getIs_have_repayplan().equals("1")) {
                    vh.btn_repay_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.feedback_green_submit_bg));
                    vh.btn_repay_list.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), RepaymentListActivity.class);
                            intent.putExtra("id", item.getPrj_order_id());
                            startActivity(intent);
                        }
                    });
                } else {
                    vh.btn_repay_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_cancel));
                    vh.btn_repay_list.setOnClickListener(null);
                }

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        args.putString("prj_id", item.getPrj_id());
                        GoClassUtil.goClass = FinanceProjectDetailActivity.class.getName();
                        GoClassUtil.TestLoginGoClass = FinanceProjectDetailActivity.class.getName();
                        MemorySave.MS.args = args;
                        new TestLogin(getActivity()).testIt((TRJActivity) getActivity());
                    }
                });
            } else {
                View view = View.inflate(getActivity(), R.layout.loading_item, null);
                loadMore();
                return view;
            }
            return convertView;
        }


    }


    public class ViewHolderps {
        TextView tv_prj_type;
        TextView tv_prj_name;
        TextView tv_date_next_repayment;
        View ll_nextview;

        ImageView im_transfer;
        ImageView im_transfer_cn;
        ImageView im_new;
        ImageView iv_activity;
        ImageView iv_limit;
        ImageView iv_phone_spec;
        ImageView im_bianxian;

        TextView content_tv_1;
        TextView content_tv_2;
        TextView content_tv_4;
        TextView content_tv_5;
        TextView tv_nv;

        Button btn_view_arg;
        Button btn_repay_list;
        Button btn_transfer;

        View ll_content;
        TextView tv_content;
        ImageView iv_blue_flag;
        TextView tv_top_1;

        TextView title_tv_1;
        TextView title_tv_2;
        TextView title_tv_4;
        TextView title_tv_5;

        ProgressBar pb;
    }


    public class ViewHolders {

        TextView tv_top_content_right;
        TextView title_tv_3;
        TextView repay_status;
        TextView tv_prj_type;
        TextView tv_prj_name;
        TextView tv_date_next_repayment;
        View ll_nextview;

        ImageView im_transfer;
        ImageView im_transfer_cn;
        ImageView im_new;
        ImageView iv_activity;
        ImageView iv_limit;
        ImageView iv_phone_spec;
        ImageView im_bianxian;

        TextView content_tv_1;
        TextView content_tv_2;
        TextView content_tv_3;
        TextView content_tv_4;
        TextView content_tv_5;
        TextView content_tv_6;
        TextView content_tv_7;
        TextView content_tv_8;
        TextView tv_nv;

        Button btn_view_arg;
        Button btn_repay_list;
        Button btn_transfer;

        View ll_content;
        TextView tv_content;
        ImageView iv_blue_flag;
        TextView tv_top_1;

        TextView title_tv_1;
        TextView title_tv_2;
        TextView title_tv_4;
        TextView title_tv_5;
        TextView title_tv_6;

        ImageView title_tv_2_img;
        ImageView iv_pre_sale;
        ProgressBar pb;
    }

    @Override
    public void callPayCheckBack(boolean success) {
        if (success) {
            loadData();
        }
    }

    @Override
    public void doInvestSuccess(boolean success) {
        if (success) {
            loadData();
            if (prjCi != null) prjCi.prjCount();
        }
    }

    /**
     * 变现check
     *
     * @param prj_order_id 订单的ID
     * @param cash_money   投资的金额
     * @param cash_rate    利率
     */
    private void cashCheck(final String prj_order_id, String cash_money, String cash_rate, final String prjName) {
//    	hccs.gainCashCheck(prj_order_id, cash_money,cash_rate,prjName);
        hccas.gainCashCheckAddResult(prj_order_id, cash_money, cash_rate);
        mProgressBar.setVisibility(View.VISIBLE);
    }


    /**
     * 变现check
     *
     * @param prj_order_id 订单的ID
     */
    private void cancelCheck(final String prj_order_id) {
        mProgressBar.setVisibility(View.VISIBLE);
        hcccs.gainCancelCash(prj_order_id);
    }

    @Override
    public void gainManageFinanceListInfoItemsuccess(
            ManageFinanceListInfoItemJson response) {
        try {
            if (response != null) {
                String boolen = response.getBoolen();
                if (boolen.equals("1")) {
                    List<ManageFinanceListInfoItem> jarray = response.getData().getList();
                    //mAdapter.clear();
                    if (jarray != null) {
                        hasMore = jarray.size() >= mPagesize;

                        if (isTransfer) {
                            if (hasMore)
                                if (StringUtils.isInteger(response.getData().getTotal_page()) && StringUtils.isInteger(response.getData().getCurrent_page())) {
                                    int count = Integer.parseInt(response.getData().getTotal_page());
                                    if (Integer.parseInt(response.getData().getCurrent_page()) >= count) {
                                        hasMore = false;
                                    }
                                } else {
                                    hasMore = false;
                                }
                            mTransferAdapter.addAll(jarray);
                        } else {
                            if (hasMore)
                                if (response.getData().getPage() != null && StringUtils.isInteger(response.getData().getPage().getTotal_page()) && StringUtils.isInteger(response.getData().getPage().getCurrent_page())) {
                                    int count = Integer.parseInt(response.getData().getPage().getTotal_page());
                                    if (Integer.parseInt(response.getData().getPage().getCurrent_page()) >= count) {
                                        hasMore = false;
                                    }
                                } else {
                                    hasMore = false;
                                }
                            mAdapter.addAll(jarray);
                        }
                    }
                } else {
                    hasMore = false;
                    String message = response.getMessage();
                    ToastUtil.showToast(getActivity(), message);
                }
                if (isTransfer) {
                    if (mTransferAdapter.mfilelist.size() <= 0 && !(response.getLogined() != null && response.getLogined().equals("0"))) {
                        contextView.findViewById(R.id.listView).setVisibility(View.GONE);
                        contextView.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                    } else {
                        contextView.findViewById(R.id.rl_empty).setVisibility(View.GONE);
                        contextView.findViewById(R.id.listView).setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mAdapter.mfilelist.size() <= 0 && !(response.getLogined() != null && response.getLogined().equals("0"))) {
                        contextView.findViewById(R.id.listView).setVisibility(View.GONE);
                        contextView.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
                    } else {
                        contextView.findViewById(R.id.rl_empty).setVisibility(View.GONE);
                        contextView.findViewById(R.id.listView).setVisibility(View.VISIBLE);
                    }
                }

            } else {
                hasMore = false;
            }
        } catch (Exception e) {
            hasMore = false;
            e.printStackTrace();
        } finally {
            mProgressBar.setVisibility(View.GONE);
            if (isTransfer) {
                mTransferAdapter.notifyDataSetChanged();
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void gainManageFinanceListInfoItemfail() {
        ToastUtil.showToast(getActivity(), "网络不给力!");
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void doInvestCheckSuccess(boolean success, FinanceInvestPBuyCheckData model) {

    }

    @Override
    public void gainCancelCashsuccess(BaseJson response) {
        try {
//      	   JSONObject jobj=response.optJSONObject("data");
            String boolen = response.getBoolen();
            if (boolen.equals("1")) {
                ToastUtil.showToast(getActivity(), "取消成功!");
                doInvestSuccess(true);
            }
            //mAdapter.clear();
            //setView(item);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void gainCancelCashfail() {
        ToastUtil.showToast(getActivity(), "网络不给力!");
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void gainCashCheckAddSuccess(CashCheckAddJson response) {
        try {
            CashCheckAddData jobj = response.getData();
            Intent intent = new Intent();
            intent.putExtra("prj_name", jobj.getOriginal_show_prj_name());
            intent.putExtra("prj_order_id", jobj.prj_order_id);
            intent.putExtra("prj_id", jobj.getPrj_id());
            intent.putExtra("rate_tips", jobj.getRate_tips());
            intent.putExtra("balance_rate", jobj.getBalance_rate());
            intent.putExtra("max_cash_money", jobj.getMax_cash_money());
            intent.setClass(getActivity(), CashActivity.class);
            getActivity().startActivity(intent);
            //setView(item);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void gainCashCheckAddFail() {
        ToastUtil.showToast(getActivity(), "网络不给力!");
        mProgressBar.setVisibility(View.GONE);
    }

}
