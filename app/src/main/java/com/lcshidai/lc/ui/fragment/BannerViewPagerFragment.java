package com.lcshidai.lc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.more.RecommendImpl;
import com.lcshidai.lc.model.more.RecommendDataItem;
import com.lcshidai.lc.model.more.RecommendImgData;
import com.lcshidai.lc.model.more.RecommendListJson;
import com.lcshidai.lc.service.HttpServiceURL;
import com.lcshidai.lc.service.more.HttpRecommendService;
import com.lcshidai.lc.ui.MainWebActivity;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.DensityUtil;
import com.lcshidai.lc.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *  @author  admin
 *  @time  2017/7/26
 *  @describe  轮播图实现页面
 */

public class BannerViewPagerFragment extends TRJFragment implements HttpServiceURL, RecommendImpl {

    private TextView banner_title_tv;
    private int currentItem = 0; // 当前图片的索引号
    private String[] bannerUrl;
    private String[] bannerTitle;
    private ViewPager viewPager;
    private BannerPagerAdapter bannerPagerAdapter;
    private List<ImageView> imageViews; // 滑动的图片集合
    private String[] titles; // 图片标题
    private List<View> dots; // 图片标题正文的那些点
    private int screenWidth;
    private LinearLayout dotsLL; // 小圆点的容器

    private FrameLayout banner_fl, banner_loading;
    TRJActivity mContext;
    private ScheduledExecutorService scheduledExecutorService;
    private ImageView imgFault;
    private HttpRecommendService hrs;
    private Boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (TRJActivity) getActivity();
        hrs = new HttpRecommendService(mContext, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(
                getResources().getLayout(R.layout.fragment_bannervp),
                container, false);
        initView(mView);

        return mView;
    }

    public void loadData() {
        hrs.gainRecommend("38");
    }

    @Override
    public void onResume() {
        if (!NetUtils.isNetworkConnected(getActivity())) {
            banner_loading.setVisibility(View.GONE);
        }

        viewPager.setCurrentItem(0);// 切换当前显示的图片
        hrs.gainRecommend("38");
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 6, 6,
                TimeUnit.SECONDS);
        super.onResume();

    }

    public void initView(View mView) {
        banner_fl = (FrameLayout) mView.findViewById(R.id.more_banner_framelayout);
        banner_loading = (FrameLayout) mView.findViewById(R.id.more_banner_loading);
        viewPager = (ViewPager) mView.findViewById(R.id.more_banner_vp);
        dotsLL = (LinearLayout) mView.findViewById(R.id.more_dots_ll);
        banner_title_tv = (TextView) mView.findViewById(R.id.more_banner_title_tv);
        bannerPagerAdapter = new BannerPagerAdapter();
        imageViews = new ArrayList<ImageView>();
        dots = new ArrayList<View>();
        viewPager.setAdapter(bannerPagerAdapter);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        imgFault = (ImageView) mView.findViewById(R.id.imgFault);
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onPause() {
        scheduledExecutorService.shutdown();
        flag = false;
        bannerHandler.removeMessages(0);
        super.onPause();
    }

    @Override
    public void onStop() {
        // 当Activity不可见的时候停止切换
        flag = false;

        bannerHandler.removeMessages(0);
        super.onStop();
    }

    /**
     * 换页切换任务
     *
     * @author Administrator
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (viewPager) {
                //if(!flag){
                currentItem = (currentItem + 1) % imageViews.size();
                bannerHandler.obtainMessage().sendToTarget(); // 通过Handler切换图片
//				}else{
//				flag=false;
//				currentItem = 0;
//				bannerHandler.obtainMessage().sendToTarget(); // 通过Handler切换图片
//				}
            }
        }

    }

    // 切换当前显示的图片
    private Handler bannerHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (flag) {
                viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
            } else {
                bannerHandler.removeMessages(0);
                viewPager.setCurrentItem(0);
                flag = true;
            }
        }
    };

    /**
     * 填充ViewPager页面的适配器
     *
     * @author Administrator
     */
    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));
            return imageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author Administrator
     */
    private class MyPageChangeListener implements OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            banner_title_tv.setText(titles[position]);
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    /**
     * 初始化焦点图的控件
     */
    private void initFocusView(int num) {
        titles = new String[num];
        if (num > 1) {
            // 设置一个监听器，当ViewPager中的页面改变时调用
            viewPager.setOnPageChangeListener(new MyPageChangeListener());
        }
        imageViews.clear();
        dots.clear();
        dotsLL.removeAllViews();
        for (int i = 0; i < num; i++) {
            // 初始化小圆点控件
            View view = new View(mContext);
            dotsLL.addView(view);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(
                    mContext, 5));
            lp.setMargins(DensityUtil.dip2px(mContext, 5F), 0,
                    DensityUtil.dip2px(mContext, 5F), 0);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.dot_focused);
            } else {
                view.setBackgroundResource(R.drawable.dot_normal);
            }
            view.setLayoutParams(lp);
            dots.add(view);

            // 初始化图片资源
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageViews.add(imageView);
        }

        // 设置焦点图点击事件
        if (null != imageViews && imageViews.size() > 0) {
            for (int i = 0; i < imageViews.size(); i++) {
                imageViews.get(i).setOnClickListener(
                        new BannerOnClickListener(i));
            }
        }

        bannerPagerAdapter.notifyDataSetChanged();
    }

    /**
     * banner图片点击事件
     */
    class BannerOnClickListener implements OnClickListener {

        int position;

        BannerOnClickListener(int pos) {
            this.position = pos;
        }

        @Override
        public void onClick(View v) {
            int pos = position % bannerUrl.length;
            if (null != bannerUrl[pos] && !"".equals(bannerUrl[pos])) {
                Intent intent = new Intent(mContext, MainWebActivity.class);
                intent.putExtra("title", bannerTitle[pos]);

                intent.putExtra("web_url", bannerUrl[pos]);
                mContext.startActivity(intent);
            }
        }

    }

    @Override
    public void gainRecommendsuccess(RecommendListJson response) {
        try {
            if (null != response) {
                String boolen = response.getBoolen();
                if ("1".equals(boolen)) {

                    if (null != response.getList()) {
                        int size = response.getList().size();
                        if (size > 0) {
                            if (bannerUrl == null) {
                                bannerUrl = new String[size];
                                bannerTitle = new String[size];
                            }

                            initFocusView(size);

                            for (int i = 0; i < size; i++) {
                                RecommendDataItem data = response.getList().get(i);
                                RecommendImgData thumb = data.getImg();
                                String urlKey = "";
                                if (screenWidth < 480) {
                                    urlKey = thumb.getAttach().getUrl_s300();
                                } else if (screenWidth >= 480 && screenWidth < 700) {
                                    urlKey = thumb.getAttach().getUrl_s700();
                                } else {
                                    urlKey = thumb.getAttach().getUrl();
                                }
                                bannerUrl[i] = data.getHref();
                                bannerTitle[i] = data.getTitle();
                                final ImageView tempImageView = imageViews.get(i);
                                Glide.with(mContext)
                                        .load("http:" + urlKey)
//                                        轮播图默认图片
                                        .placeholder(R.drawable.banner_default)
                                        .into(new GlideDrawableImageViewTarget(tempImageView) {
                                            @Override
                                            public void onResourceReady(GlideDrawable resource,
                                                                        GlideAnimation<? super GlideDrawable> animation) {
                                                super.onResourceReady(resource, animation);
                                                if (banner_loading.getVisibility() == View.GONE) {
                                                    for (int index = 0; index < imageViews.size(); index++) {
                                                        if (imageViews.get(index) == tempImageView) {
                                                            currentItem = index;
                                                            bannerHandler.sendEmptyMessage(0);
                                                            break;
                                                        }
                                                    }
                                                    banner_loading.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }
                            banner_fl.setVisibility(View.VISIBLE);
                            imgFault.setVisibility(View.GONE);
                        } else {
                            banner_fl.setVisibility(View.GONE);
                            imgFault.setImageResource(R.drawable.banner_default);
                        }
                    } else {
                        banner_fl.setVisibility(View.GONE);
                        imgFault.setImageResource(R.drawable.banner_default);
                    }

                }
            }
            banner_loading.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
            banner_loading.setVisibility(View.GONE);
            imgFault.setImageResource(R.drawable.banner_default);
        }
    }

    @Override
    public void gainRecommendfail() {
        banner_loading.setVisibility(View.GONE);
        imgFault.setImageResource(R.drawable.banner_default);
    }
}
