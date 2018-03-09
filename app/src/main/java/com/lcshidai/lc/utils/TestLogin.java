package com.lcshidai.lc.utils;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.lcshidai.lc.impl.TestLoginImpl;
import com.lcshidai.lc.model.Loading.TestLoginJson;
import com.lcshidai.lc.service.HttpTestLoginService;
import com.lcshidai.lc.ui.base.TRJActivity;

public class TestLogin implements TestLoginImpl {

    public FragmentActivity mContext;
    HttpTestLoginService htls;
//	public static String TEST_LOGIN_URL="Mobile2/Auth/isLogin2";

    public TestLogin(FragmentActivity context) {
        mContext = context;
        htls = new HttpTestLoginService((TRJActivity) mContext, this);
    }

    public void testIt(TRJActivity content) {
        htls.gainTestLogin();
    }

    @Override
    public void gainTestLoginsuccess(TestLoginJson response) {
        try {
            String boolen = response.getBoolen();
            String logined = response.getLogined();
            if (!boolen.equals("1")) return;

            if (logined != null && logined.trim().equals("0")) return;

            Class clazz;
            String nameStr = GoClassUtil.TestLoginGoClass;
            GoClassUtil.TestLoginGoClass = null;
            if (nameStr != null && !nameStr.equals("")) {
                Intent intent = mContext.getIntent();
                clazz = Class.forName(nameStr);
                mContext.getIntent().setClass(mContext, clazz);
                if (MemorySave.MS.args != null) mContext.getIntent().putExtras(MemorySave.MS.args);
                mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    public void gainTestLoginfail() {
        mContext.getIntent().removeExtra("goClass");
    }

}
