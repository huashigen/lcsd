package com.lcshidai.lc.ui.account;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.CheckSafeQuestionImpl;
import com.lcshidai.lc.impl.account.SetSafeQuestionImpl;
import com.lcshidai.lc.impl.account.VerifySafeQuestionImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.service.account.HttpCheckSafeQuestionService;
import com.lcshidai.lc.service.account.HttpSetSafeQuestionService;
import com.lcshidai.lc.service.account.HttpVerifySafeQuestionService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.ToastUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 安全保护问题
 *
 * @author 000853
 */
public class SafeQuestionActivity extends TRJActivity implements OnClickListener, CheckSafeQuestionImpl, SetSafeQuestionImpl, VerifySafeQuestionImpl {
    HttpCheckSafeQuestionService hcsqs;
    HttpSetSafeQuestionService hssqs;
    HttpVerifySafeQuestionService hvsqs;

    private int setting_flag;    //状态	0-未设置		1-已设置
    private int intentFlag = -1;    //跳转状态	1-从修改支付密码那里跳转

    private Context mContext;
    private ImageButton mBackBtn;
    private TextView mTvTitle, mSubTitle;
    private Button mSaveBtn;

    private LinearLayout ll_question_select, ll_main;
    private EditText et_answer;
    private TextView bt_submit;
    private TextView tv_question;

    private Dialog loading;
    private String selectQuestionCodeNo = "";
    private String questionName = "";

    private Dialog timeUpDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        intentFlag = getIntent().getIntExtra("intent_flag", -1);
        hcsqs = new HttpCheckSafeQuestionService(this, this);
        hssqs = new HttpSetSafeQuestionService(this, this);
        hvsqs = new HttpVerifySafeQuestionService(this, this);
        initView();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initView() {
        mContext = this;
        setContentView(R.layout.activity_safe_question);

        mBackBtn = (ImageButton) findViewById(R.id.btn_back);
        mBackBtn.setOnClickListener(this);
        mTvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        mTvTitle.setText("安全保护问题");
        mSubTitle = (TextView) findViewById(R.id.tv_subtitle);
        mSubTitle.setVisibility(View.GONE);
        mSaveBtn = (Button) findViewById(R.id.btn_option);
        mSaveBtn.setVisibility(View.INVISIBLE);

        ll_main = (LinearLayout) findViewById(R.id.safe_question_ll_main);
        ll_question_select = (LinearLayout) findViewById(R.id.safe_question_ll_select);
        et_answer = (EditText) findViewById(R.id.safe_question_et_answer);
        bt_submit = (TextView) findViewById(R.id.safe_question_bt_submit);
        tv_question = (TextView) findViewById(R.id.safe_question_tv_question);

        ll_question_select.setOnClickListener(this);
        bt_submit.setOnClickListener(this);

        timeUpDialog = createDialog("提示", "操作超时，请重新验证您的原安全保护问题。", "确    定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectQuestionCodeNo = "";
                questionName = "";
                statusInit("1");
                timeUpDialog.dismiss();
            }
        });

        loading = createLoadingDialog(mContext, "数据加载中", true);

        if (intentFlag != 1) {
            loading.show();
            checkIsSetting();
        } else {
            statusInit("0");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                SafeQuestionActivity.this.finish();
                break;
            case R.id.safe_question_ll_select:
                Intent intent = new Intent(mContext, SafeQuestionSelectActivity.class);
                intent.putExtra("select_quetion_code_no", selectQuestionCodeNo);
                intent.putExtra("question_name", questionName);
                startActivityForResult(intent, 10);
                break;
            case R.id.safe_question_bt_submit:

                if ("".equals(selectQuestionCodeNo)) {
                    createDialogDismissAuto("请选择问题");
                    return;
                }
                if ("".equals(et_answer.getText().toString())) {
                    createDialogDismissAuto("请填写问题答案");
                    return;
                }
                if (!loading.isShowing()) {
                    loading.show();
                }

                //设置问题
                if (setting_flag == 0) {
                    setSafeQuestion();
                }
                //修改问题
                else {
                    verifySafeQuestion();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 20) {
            selectQuestionCodeNo = data.getStringExtra("select_quetion_code_no");
            if (!"".equals(selectQuestionCodeNo)) {
                questionName = data.getStringExtra("question_name");
                tv_question.setText(questionName);
                tv_question.setTextColor(Color.parseColor("#333333"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 判读是否设置过安全提示问题
     */
    private void checkIsSetting() {
        hcsqs.gainCheckSafeQuestion();
    }

    /**
     * 提交安全保护问题
     */
    private void setSafeQuestion() {
        hssqs.gainSetSafeQuestion(selectQuestionCodeNo, et_answer);
    }

    /**
     * 验证安全保护问题
     */
    private void verifySafeQuestion() {
        hvsqs.gainVerifySafeQuestion(selectQuestionCodeNo, et_answer);
    }

    /**
     * 设置或修改页面初始化
     * flag: 0-未设置		1-已设置 	2-修改验证过后输入新的安保
     */
    private void statusInit(String flag) {
        if ("0".equals(flag)) {
            setting_flag = 0;
//			mTvTitle.setText("设置安全保护问题");
            tv_question.setText("选择问题");
            tv_question.setTextColor(Color.parseColor("#7E7E7E"));
            bt_submit.setText("保存");
            et_answer.setText("");

        } else if ("1".equals(flag)) {
            setting_flag = 1;
//			mTvTitle.setText("修改安全保护问题");
            tv_question.setText("选择原安全保护问题");
            tv_question.setTextColor(Color.parseColor("#7E7E7E"));
            bt_submit.setText("下一步");
            et_answer.setText("");
        } else {
            setting_flag = 0;
//			mTvTitle.setText("修改安全保护问题");
            tv_question.setText("选择问题");
            tv_question.setTextColor(Color.parseColor("#7E7E7E"));
            bt_submit.setText("保存");
            et_answer.setText("");
        }
        ll_main.setVisibility(View.VISIBLE);
    }

    @Override
    public void gainVerifySafeQuestionsuccess(BaseJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                if ("1".equals(boolen)) {
                    createDialogDismissAuto("验证成功");
                    selectQuestionCodeNo = "";
                    questionName = "";
                    statusInit("2");
                } else {
                    createDialogDismissAuto(response.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainVerifySafeQuestionfail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        createDialogDismissAuto("网络不给力！");
    }

    @Override
    public void gainSetSafeQuestionsuccess(BaseJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                if ("1".equals(boolen)) {
                    ToastUtil.showToast(this, "保存成功！");
                    setResult(18);
                    SafeQuestionActivity.this.finish();
                } else if ("-1".equals(boolen)) {
                    if (!timeUpDialog.isShowing()) {
                        timeUpDialog.show();
                    }
                } else {
                    createDialogDismissAuto(response.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainSetSafeQuestionfail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        createDialogDismissAuto("网络不给力！");
    }

    @Override
    public void gainCheckSafeQuestionsuccess(BaseJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                statusInit(boolen);
                if (loading.isShowing()) {
                    loading.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gainCheckSafeQuestionfail() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
        createDialogDismissAuto("网络不给力！");
    }

}
