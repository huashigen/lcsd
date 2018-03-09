package com.lcshidai.lc.ui.more;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.impl.more.InviteImpl;
import com.lcshidai.lc.model.more.InviteData;
import com.lcshidai.lc.model.more.InviteJson;
import com.lcshidai.lc.service.more.HttpInviteService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.OneKeyShareUtil;
import com.lcshidai.lc.utils.StringUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 邀请好友
 */
public class InviteActivity extends TRJActivity implements InviteImpl {

    @Bind(R.id.ib_top_bar_back)
    ImageButton ibTopBarBack;
    @Bind(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;
    @Bind(R.id.tv_recommend_profit)
    TextView tvRecommendProfit;
    @Bind(R.id.tv_recommend_zd)
    TextView tvRecommendZd;
    @Bind(R.id.tv_invite_title)
    TextView tvInviteTitle;
    @Bind(R.id.tv_view_invite_detail)
    TextView tvViewInviteDetail;
    @Bind(R.id.iv_qr_code)
    ImageView ivQrCode;
    @Bind(R.id.tv_recommend_code)
    TextView tvRecommendCode;
    @Bind(R.id.tv_invite_info)
    TextView tvInviteInfo;
    @Bind(R.id.tv_invite_info2)
    TextView tvInviteInfo2;
    @Bind(R.id.btn_my_invite)
    Button btnMyInvite;
    @Bind(R.id.btn_invite)
    Button btnInvite;
    @Bind(R.id.tv_invite_foot_des)
    TextView tvInviteFootDes;

    private HttpInviteService his;
    private OneKeyShareUtil oneKeyShareUtil;
    private String logo = "";
    private String share_content = ""; // 分享内容
    private String share_url = ""; // 分享链接地址
    private String share_title = ""; // 分享标题
    private String share_desn = ""; // 分享标题
    private Dialog loading;
    private int mainWebFlag = 0; // 是否是从MainWebActivtiy跳转过来，22表示是

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);
        initView();
        his = new HttpInviteService(this, this);
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        tvTopBarTitle.setText(getResources().getString(R.string.more_str12));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWith = dm.widthPixels;
        LayoutParams lp = (LayoutParams) ivQrCode.getLayoutParams();
        lp.width = (int) (screenWith * 0.55);
        lp.height = (int) (screenWith * 0.55);
        ivQrCode.setLayoutParams(lp);

        oneKeyShareUtil = new OneKeyShareUtil(mContext);
        loading = createLoadingDialog(mContext, getResources().getString(R.string.loading), true);
        loading.show();

    }

    private void loadData() {
        his.gainInvite();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mainWebFlag == 22) {
                setResult(23);
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void getInviteInfoSuccess(InviteJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {
                    InviteData dataObj = response.getData();
                    StringBuffer sbred = new StringBuffer();
                    sbred.append(dataObj.getExt_data()[0]);
                    String str = sbred.toString();
                    sbred.append(dataObj.getExt_data()[1]);
                    tvRecommendProfit.setText(dataObj.getRecommend_money());
                    tvRecommendZd.setText(dataObj.getRecommend_zd());
                    tvInviteFootDes.setText(dataObj.getDesc());
                    SpannableString ss = new SpannableString(sbred);
                    final String ext_url[] = dataObj.getExt_url();
                    ss.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            if (ext_url != null && ext_url[0] != null) {
                                Intent intent = new Intent(mContext, MainWebActivity.class);
                                intent.putExtra("web_url", ext_url[0]);
                                startActivity(intent);
                            }
                        }
                    }, str.length(), sbred.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(getResources().getColor(
                            R.color.login_orange)), str.length(), sbred.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvViewInviteDetail.setMovementMethod(LinkMovementMethod.getInstance());
                    tvViewInviteDetail.setText(ss);
                    tvRecommendCode.setText(dataObj.getRecommend_code());
                    String img_url = dataObj.getQrcode();
                    logo = dataObj.getLogo();
                    share_content = dataObj.getContent();
                    share_desn = dataObj.getDesc();
                    String desc = dataObj.getDesc();
                    if (!StringUtils.isEmpty(desc) && desc.contains(getString(R.string.more_str15))) {
                        String[] strs = desc.split(getString(R.string.more_str15));
                        tvInviteInfo.setText(String.format("%s%s", strs[0], getString(R.string.more_str15)));
                        tvInviteInfo2.setText(strs[1]);
                    } else {
                        tvInviteInfo.setText(desc);
                        tvInviteInfo2.setVisibility(View.GONE);
                    }
                    share_title = dataObj.getTitle();
                    share_url = dataObj.getUr();
                    Glide.with(mContext).load(img_url).into(ivQrCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (loading.isShowing()) {
                loading.dismiss();
            }
        }

    }

    @Override
    public void getInviteInfoFailed() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        showToast(getResources().getString(R.string.net_error));
    }

    @OnClick({R.id.ib_top_bar_back, R.id.btn_invite, R.id.btn_my_invite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_top_bar_back:
                if (mainWebFlag == 22) {
                    setResult(23);
                }
                InviteActivity.this.finish();
                break;
            case R.id.btn_invite:
                oneKeyShareUtil.toShareForImageUrlMSG(share_content, share_desn, share_title,
                        share_url, logo, new PlatformActionListener() {

                            @Override
                            public void onError(Platform arg0, int arg1, Throwable arg2) {
                                showToast(getResources().getString(R.string.more_str13));
                            }

                            @Override
                            public void onComplete(Platform arg0, int arg1,
                                                   HashMap<String, Object> arg2) {
                                showToast(getResources().getString(R.string.more_str14));
                            }

                            @Override
                            public void onCancel(Platform arg0, int arg1) {
                            }
                        });
                break;
            case R.id.btn_my_invite:
                Intent intent = new Intent();
                intent.putExtra("web_url", LCHttpClient.BASE_WAP_HEAD + "/#/inviteList");
                intent.putExtra("title", "我的推荐");
                intent.setClass(InviteActivity.this, MainWebActivity.class);
                startActivity(intent);
                break;
        }
    }
}
