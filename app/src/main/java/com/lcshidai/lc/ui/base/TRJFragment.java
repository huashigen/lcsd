package com.lcshidai.lc.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lcshidai.lc.R;
import com.lcshidai.lc.http.LCHttpClient;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.utils.UpdateManager;

public class TRJFragment extends Fragment {

    protected Dialog loadingDialog = null;
    protected Context mContext = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (!NetUtils.isNetworkConnected(getActivity())) {
            String message = "网络连接异常,请检查网络";
            try {
                ToastUtil.showToast(getActivity(), message);
            } catch (Exception e) {
            }
        }
        PackageManager manager = getActivity().getPackageManager();
        ApplicationInfo app_info = null;
        try {
            app_info = manager.getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String brand = android.os.Build.BRAND;// 手机品牌
        String model = android.os.Build.MODEL;// 手机型号
        String channel = app_info.metaData.getString("TD_CHANNEL_ID");
        params.put("hmsr", channel);
        params.put("brand", brand);
        params.put("model", model);
        LCHttpClient.post(getActivity(), url, params, responseHandler);
    }

    /**
     * 显示加载dialog
     */
    protected void showLoadingDialog(String message, boolean cancelAble) {
        if (loadingDialog == null) {
            loadingDialog = createLoadingDialog(mContext, message, cancelAble);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 隐藏加载dialog
     */
    protected void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private Dialog createLoadingDialog(Context context, String message, boolean cancelAble) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_custmor_loading, null);// 得到加载view
        TextView tipTextView = (TextView) view.findViewById(R.id.custmor_loadding_tv_message);// 提示文字
        tipTextView.setText(message);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.style_loading_dialog);// 创建自定义样式dialog
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(cancelAble);
        return loadingDialog;
    }

    /**
     * 判断是否有新的版本
     */
    protected void checkAppUpdate() {
        UpdateManager.reset();
        UpdateManager.getInstance(getActivity()).checkUpdateInfo(getActivity(), 0);
    }
}
