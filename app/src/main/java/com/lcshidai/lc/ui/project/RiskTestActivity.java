package com.lcshidai.lc.ui.project;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.licai.RiskTestImpl;
import com.lcshidai.lc.model.BaseJson;
import com.lcshidai.lc.model.licai.AnswerItem;
import com.lcshidai.lc.model.licai.RiskQAData;
import com.lcshidai.lc.model.licai.RiskTestQuestionJson;
import com.lcshidai.lc.service.licai.HttpRiskTestService;
import com.lcshidai.lc.ui.adapter.base.ListViewDataAdapter;
import com.lcshidai.lc.ui.adapter.base.ViewHolderBase;
import com.lcshidai.lc.ui.adapter.base.ViewHolderCreator;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.utils.CommonUtil;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 风险测试 Activity
 */
public class RiskTestActivity extends TRJActivity implements View.OnClickListener, RiskTestImpl {

    private TextView tvAnswerQuestionIndicator;
    private TextView tvQuestion;
    private ListView lvAnswers;
    private TextView tvFinishRiskTest;
    private int currentNum = 0;

    private Dialog riskTestExitDialog, riskTestFinishDialog, riskTestDeclareDialog;

    private HttpRiskTestService riskTestService;
    private StringBuilder questionBuilder, answerBuilder;

    private ArrayList<RiskQAData> qaDataList;
    private ListViewDataAdapter<AnswerItem> riskTestAnswerAdapter;
    private boolean isNotTestClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_test);

        riskTestService = new HttpRiskTestService(this, this);

        initViewsAndEvents();
    }

    @Override
    protected boolean isNotApplyTranslucent() {
        return false;
    }

    private void initExitDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_risk_test_exit, null);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riskTestExitDialog.dismiss();
                // clear already stored answers
                finish();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riskTestExitDialog.dismiss();
            }
        });

        riskTestExitDialog = new Dialog(this, R.style.style_loading_dialog);
        riskTestExitDialog.setContentView(view);
        riskTestExitDialog.setCancelable(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = riskTestExitDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
        riskTestExitDialog.getWindow().setAttributes(lp);
    }

    /**
     * 初始化风险测试完成Dialog
     */
    private void initFinishDialog(String resultMessage) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_risk_test_finish, null);
        TextView tvTextResult = (TextView) view.findViewById(R.id.tv_test_result);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        if (!CommonUtil.isNullOrEmpty(resultMessage)) {
            tvTextResult.setText(resultMessage);
        }
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riskTestFinishDialog.dismiss();
                finish();
            }
        });

        riskTestFinishDialog = new Dialog(this, R.style.style_loading_dialog);
        riskTestFinishDialog.setContentView(view);
        riskTestFinishDialog.setCancelable(true);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = riskTestFinishDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.8); //设置宽度
        riskTestFinishDialog.getWindow().setAttributes(lp);
    }

    /**
     * 初始化重要声明Dialog
     */
    private void initRiskTestDeclareDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_dialog_risk_test_declare, null);
        TextView tvTestRightNow = (TextView) view.findViewById(R.id.tv_test_right_now);
        TextView tvNotTest = (TextView) view.findViewById(R.id.tv_not_test);

        tvTestRightNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riskTestDeclareDialog.dismiss();
            }
        });

        tvNotTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riskTestDeclareDialog.dismiss();
                isNotTestClicked = true;
                riskTestService.saveRiskTestAnswer(GoLoginUtil.getAccessToken((TRJActivity) mContext),
                        GoLoginUtil.getUserToken((TRJActivity) mContext), "1", "1");
            }
        });

        riskTestDeclareDialog = new Dialog(this, R.style.style_loading_dialog);
        riskTestDeclareDialog.setContentView(view);
        riskTestDeclareDialog.setCancelable(false);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = riskTestDeclareDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.9); //设置宽度
        riskTestDeclareDialog.getWindow().setAttributes(lp);
    }

    private int mSelectPos = -1;

    private void initViewsAndEvents() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.ib_top_bar_back);
        TextView tvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        tvAnswerQuestionIndicator = (TextView) findViewById(R.id.tv_answer_question_indicator);
        tvQuestion = (TextView) findViewById(R.id.tv_question_content);
        lvAnswers = (ListView) findViewById(R.id.lv_answer);
        tvFinishRiskTest = (TextView) findViewById(R.id.tv_finish_risk_test);
        tvFinishRiskTest.setOnClickListener(this);
        // 显示重要声明弹窗
        if (riskTestDeclareDialog == null) {
            initRiskTestDeclareDialog();
            riskTestDeclareDialog.show();
        } else {
            riskTestDeclareDialog.show();
        }
        if (!CommonUtil.isNullOrEmpty(GoLoginUtil.getAccessToken((TRJActivity) mContext)) &&
                !CommonUtil.isNullOrEmpty(GoLoginUtil.getUserToken((TRJActivity) mContext))) {
            riskTestService.getRiskTestQuestionList(GoLoginUtil.getAccessToken((TRJActivity) mContext),
                    GoLoginUtil.getUserToken((TRJActivity) mContext));
        }

        btnBack.setOnClickListener(this);
        tvTitle.setText(R.string.risk_test);
        questionBuilder = new StringBuilder();
        answerBuilder = new StringBuilder();
        riskTestAnswerAdapter = new ListViewDataAdapter<>(new ViewHolderCreator<AnswerItem>() {
            @Override
            public ViewHolderBase<AnswerItem> createViewHolder(int position) {
                return new ViewHolderBase<AnswerItem>() {
                    RelativeLayout rlAnswerItemContainer;
                    TextView tvAnswerNum;
                    TextView tvAnswerContent;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = LayoutInflater.from(mContext).inflate(R.layout.risk_test_answer_item, null);
                        rlAnswerItemContainer = (RelativeLayout) convertView.findViewById(R.id.rl_answer_item_container);
                        tvAnswerNum = (TextView) convertView.findViewById(R.id.tv_answer_number);
                        tvAnswerContent = (TextView) convertView.findViewById(R.id.tv_answer_content);
                        return convertView;
                    }

                    @Override
                    public void showData(int position, AnswerItem itemData) {
                        if (null != itemData) {
                            if (mSelectPos != -1) {
                                if (mSelectPos == position) {
                                    rlAnswerItemContainer.setBackgroundColor(getResources().getColor(R.color.bg_main_gray));
                                    tvAnswerNum.setBackgroundResource(R.drawable.risk_test_answer_number_bg_press);
                                    tvAnswerNum.setTextColor(getResources().getColor(R.color.white));
                                } else {
                                    rlAnswerItemContainer.setBackgroundColor(getResources().getColor(R.color.white));
                                    tvAnswerNum.setBackgroundResource(R.drawable.risk_test_answer_number_bg_normal);
                                    tvAnswerNum.setTextColor(getResources().getColor(R.color.theme_color));
                                }
                            } else {
                                rlAnswerItemContainer.setBackgroundColor(getResources().getColor(R.color.white));
                                tvAnswerNum.setBackgroundResource(R.drawable.risk_test_answer_number_bg_normal);
                                tvAnswerNum.setTextColor(getResources().getColor(R.color.theme_color));
                            }
                            tvAnswerNum.setText((char) ('A' + position) + "");
                            tvAnswerContent.setText(itemData.getTitle());
                        }
                    }
                };
            }
        });
        lvAnswers.setAdapter(riskTestAnswerAdapter);
        lvAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                lvAnswers.setSelection(position);
                mSelectPos = position;
                riskTestAnswerAdapter.notifyDataSetChanged();
                if (currentNum == qaDataList.size() - 1) {
                    tvFinishRiskTest.setVisibility(View.VISIBLE);
                }
                if (currentNum < qaDataList.size()) {
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(100);
                                mHandler.sendEmptyMessage(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }

            }
        });
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                // 进行选项记录
                questionBuilder.append(qaDataList.get(currentNum).getQuestion_id()).append(",");
                answerBuilder.append(riskTestAnswerAdapter.getDataList().get(mSelectPos).getAnswer_id()).append(",");
                mSelectPos = -1;
                currentNum++;
                loadSpecialQuestion(qaDataList, currentNum);
            }
        }
    };

    /**
     * 根据得到的列表加载特定的问题
     *
     * @param qaDataList         得到的问题列表
     * @param currentQuestionNum 要加载的问题编号
     */
    private void loadSpecialQuestion(ArrayList<RiskQAData> qaDataList, int currentQuestionNum) {
        // 清空之前的数据
        riskTestAnswerAdapter.getDataList().clear();
        if (null != qaDataList && qaDataList.size() > 0 && currentQuestionNum < qaDataList.size()) {
            if (currentQuestionNum < qaDataList.size()) {
                RiskQAData riskQAData = qaDataList.get(currentQuestionNum);
                if (riskQAData != null) {
                    String indicatorStr = (currentQuestionNum + 1) + "/" + qaDataList.size();
                    SpannableStringBuilder builder = new SpannableStringBuilder(indicatorStr);
                    ForegroundColorSpan yellowSpan = new ForegroundColorSpan(getResources().getColor(R.color.theme_color));
                    builder.setSpan(yellowSpan, 0, indicatorStr.indexOf("/"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvAnswerQuestionIndicator.setText(builder);
                    String title = riskQAData.getTitle();
                    tvQuestion.setText((currentQuestionNum + 1) + "." + title);
                    riskTestAnswerAdapter.getDataList().addAll(riskQAData.getAnswerItemArrayList());
                }
                if (riskTestAnswerAdapter != null) {
                    lvAnswers.setAdapter(riskTestAnswerAdapter);
                }
            }
        }
//        android.util.Log.e("QID=", questionBuilder.toString());
//        android.util.Log.e("AID=", answerBuilder.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_bar_back:
                onBackPressed();
                break;
            case R.id.tv_finish_risk_test:
                // send the record answers to server, then finish;
                // 去掉保存的问题id及答案id最后的逗号
                String question_ids = questionBuilder.deleteCharAt(questionBuilder.length() - 1).toString();
                String answer_ids = answerBuilder.deleteCharAt(answerBuilder.length() - 1).toString();
                android.util.Log.e("question_ids=", question_ids);
                android.util.Log.e("answer_ids=", answer_ids);
                riskTestService.saveRiskTestAnswer(GoLoginUtil.getAccessToken((TRJActivity) mContext),
                        GoLoginUtil.getUserToken((TRJActivity) mContext), question_ids, answer_ids);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (currentNum <= 10) {
            if (riskTestExitDialog == null) {
                initExitDialog();
                riskTestExitDialog.show();
            } else if (!riskTestExitDialog.isShowing()) {
                riskTestExitDialog.show();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void getRiskTestQuestionListSuccess(RiskTestQuestionJson response) {
        if (null != response) {
            if (null != response.getData()) {
                qaDataList = (ArrayList<RiskQAData>) response.getData().getQuestionList();
                if (null == qaDataList) {
                    // 已经测试过的如何处理
                    ToastUtil.showLongToast(mContext, response.getMessage());
                    finish();
                } else {
                    currentNum = 0;
                    loadSpecialQuestion(qaDataList, currentNum);
                }
            }
        }
    }

    @Override
    public void getRiskTestQuestionListFail() {

    }

    @Override
    public void saveRiskTestAnswerSuccess(BaseJson response) {
        // 保存日期并计算到期时间（当前时间 + 2年）
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, currentYear + 2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String dateStr = simpleDateFormat.format(calendar.getTimeInMillis());
        GoLoginUtil.saveRiskValidateTime(dateStr, this);
        if (!isNotTestClicked) {
            if (riskTestFinishDialog == null) {
                initFinishDialog(response.getMessage());
                riskTestFinishDialog.show();
            } else if (!riskTestFinishDialog.isShowing()) {
                riskTestFinishDialog.show();
            }
        } else {
            finish();
        }
    }

    @Override
    public void saveRiskTestAnswerFail(BaseJson errorResponse) {
        ToastUtil.showLongToast(mContext, errorResponse.getMessage());
        finish();
    }
}
