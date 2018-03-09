package com.lcshidai.lc.ui.fragment.more;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.account.MyRewardImpl;
import com.lcshidai.lc.model.account.MyRewardRecordData;
import com.lcshidai.lc.model.account.MyRewardRecordJson;
import com.lcshidai.lc.model.account.MyRewardUsed;
import com.lcshidai.lc.service.account.HttpMyRewardService;
import com.lcshidai.lc.ui.account.FinancialCashActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.utils.MemorySave;
import com.lcshidai.lc.utils.NetUtils;
import com.lcshidai.lc.utils.StringUtils;
import com.lcshidai.lc.utils.ToastUtil;
import com.lcshidai.lc.widget.xlvfresh.XListView;
import com.lcshidai.lc.widget.xlvfresh.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 公告列表子类
 *
 * @author 000814
 *
 */
public class GoldenTabItemFragment extends TRJFragment implements MyRewardImpl, IXListViewListener {

	private XListView mListView;
	private ItemAdapter mAdapter;
    HttpMyRewardService hmrs;

	private View mContent;
	private int mPagesize = 10;
	private int mP = 1;
	boolean hasMore;
	boolean mAdapterSetted;
	private int currentPage = 1;
	public boolean isRundata = false;

	private int nowPage = 1; // 当前页数
	private int nowPager = 1;
	private int pagesize = 10;
	public String mCurrentPType = "0";

	private int totalPages = 0;

	boolean haseMore = false;

	public ArrayList<String> idList = new ArrayList<String>();
	private View mProgressBar;
	int mPosition = -1;
	private int screenWidth;
	private boolean isLoading = false; // 是否正在加载数据
	public List<MyRewardUsed> mfilelist = new ArrayList<MyRewardUsed>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		hmrs = new HttpMyRewardService((FinancialCashActivity) getActivity(),  this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContent = inflater.inflate(R.layout.fragment_golden_lisvt_view,
				container, false);
		mAdapter = new ItemAdapter(getActivity(), mfilelist);
		mProgressBar = mContent.findViewById(R.id.progressContainer);
		mListView = (XListView) mContent.findViewById(R.id.listView);
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		mListView.setXListViewListener(this);
		return mContent;
	}

	public void ld(boolean isReload) {
		if (!isReload && mAdapter != null && mAdapter.size > 1) {
			mListView.showHeader();
		} else {
			reLoadData();
		}
	}



	public synchronized void reLoadData() {
		if (mAdapter != null && !isRundata) {

			idList.clear();

			hasMore = false;
			mAdapter.clear();
			mListView.setVisibility(View.GONE);
			mAdapter.notifyDataSetChanged();
			nowPage = 1;
			mProgressBar.setVisibility(View.VISIBLE);
			mContent.findViewById(R.id.rl_empty).setVisibility(View.GONE);
			loadData();

		}
	}


	public synchronized void reLoadDataDown() {
		if (mAdapter != null && !isRundata) {
			idList.clear();
			isDown = true;
			hasMore = false;
			mP = 1;
			nowPage = 1;
			loadData();
		}
	}

	boolean isDown = false;

	private void loadData() {
		if (getActivity() == null
				|| !NetUtils.isNetworkConnected(getActivity())) {
			mProgressBar.setVisibility(View.GONE);
			mContent.findViewById(R.id.listView).setVisibility(View.GONE);
			mContent.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
			return;
		}
		isRundata = true;
		hmrs.gainMyReward(nowPage, pagesize);

	}
	@Override
	public void onStop() {
		super.onStop();
		isRundata = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		mProgressBar.setVisibility(View.GONE);
		mPosition = -1;
		if (mAdapter != null && mAdapter.size > 1) {
			mProgressBar.setVisibility(View.GONE);
			if (MemorySave.MS.goToFinanceAll)
				mListView.showHeader();
			MemorySave.MS.goToFinanceAll = false;
		} else {
			reLoadData();
		}

	}



	public class ItemAdapter extends ArrayAdapter<MyRewardUsed> {
		private Context mContext;
		public List<MyRewardUsed> mfilelist = new ArrayList<MyRewardUsed>();
		public int size;

		public ItemAdapter(Activity context, List<MyRewardUsed> lilelist) {
			super(context, 0);
			mContext = context;
			mfilelist = lilelist;
		}

		@Override
		public int getCount() {
			size = mfilelist.size();
			return size + (hasMore ? 1 : 0);
		}

		@Override
		public void add(MyRewardUsed object) {
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
		public MyRewardUsed getItem(int position) {
			return mfilelist.get(position);
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			try {
				ViewHolders vh = null;
				if (position < size) {
					if (convertView == null || convertView.getTag() == null) {
						vh = new ViewHolders();
						convertView = LayoutInflater.from(mContext).inflate(
								R.layout.gold_have_item, null);
						vh.bp_item_change_money_tv1 = (TextView) convertView
								.findViewById(R.id.bp_item_change_money_tv1);
						vh.bp_item_change_type_tv1 = (TextView) convertView
								.findViewById(R.id.bp_item_change_type_tv1);
						vh.bp_item_title_type = (TextView) convertView
								.findViewById(R.id.bp_item_title_type);
						vh.bp_item_title_time = (ImageView) convertView
								.findViewById(R.id.bp_item_title_time);
						vh.bp_item_amount_tv = (TextView) convertView
								.findViewById(R.id.bp_item_amount_tv);
						vh.bp_item_prj_name2_tv = (TextView) convertView
								.findViewById(R.id.bp_item_prj_name2_tv);
						vh.bp_item__time = (TextView) convertView
						.findViewById(R.id.bp_item__time);
						convertView.setTag(vh);
					} else {
						vh = (ViewHolders) convertView.getTag();

					}
					MyRewardUsed pi = mfilelist.get(position);
//					vh.bp_item_change_money_tv1.setText("代收收益（元）");
//					vh.bp_item_change_type_tv1.setText(pi.getMoney());
					vh.bp_item_title_type.setText(pi.getSource());
					if(pi.getInfo().equals("已使用")){
					vh.bp_item_title_time.setImageResource(R.drawable.icon_used);
					}else if(pi.getInfo().equals("使用中")){
					vh.bp_item_title_time.setImageResource(R.drawable.icon_have);
					}
					vh.bp_item_amount_tv.setText(pi.getMoney());
					vh.bp_item_prj_name2_tv.setText(pi.getTyj_expire_time());
					vh.bp_item__time.setText(pi.getCtime());//getExpect_repay_time
					final String id = pi.getId();
				} else {
					View view = View.inflate(getActivity(),
							R.layout.loading_item, null);
					loadData();
					return view;
				}
			} catch (Exception e) {
			}
			return convertView;
		}

		public class ViewHolders {

			public TextView bp_item__time;
			public TextView bp_item_prj_name2_tv;
			public TextView bp_item_amount_tv;
			public ImageView bp_item_title_time;
			public TextView bp_item_title_type;
			public TextView bp_item_change_type_tv1;
			public TextView bp_item_change_money_tv1;
			TextView tvTitle, tvSource;
			TextView tvTime;
			ImageView iv_newsicon;
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

	@Override
	public void gainMyRewardsuccess(MyRewardRecordJson response) {
		try {

			if (response != null) {
				String boolen = response.getBoolen();
				if (boolen.equals("1")) {
					if (isDown) {
						isDown = false;
						mAdapter.clear();
						mListView.stopRefresh();
						mListView.stopLoadMore();
						mListView.setRefreshTime();
					}
					//MyRewardData dataObj = response.getData();

					if(null!=response.getData()&&null!=response.getData().getData()){
						int size = response.getData().getData().size();
						MyRewardRecordData dataObj = response.getData();
						//List<MyRewardList> dataArray = dataObj.getData();
						int totalPage = Integer.valueOf(dataObj.getTotal_page());

						currentPage = Integer.valueOf(dataObj.getCurrent_page());
						if (String.valueOf(currentPage).equals(String.valueOf(nowPage))) {//nowPage
							List<MyRewardUsed> dataArray = response.getData().getData();
							if(dataArray.size() < pagesize) {
								haseMore = false;
								mListView.setPullLoadEnable(false);
							}
							for (int i = 0; i < dataArray.size(); i++) {
								MyRewardUsed obj = dataArray.get(i);
								mAdapter.add(obj);
							}

							if (totalPage > nowPage) {
								haseMore = true;
								nowPage += 1;
							} else {
								haseMore = false;
								mListView.setPullLoadEnable(false);
							}
							isLoading = false;
						}
					}

				} else {
					mContent.findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);

				}
				if (mAdapter.mfilelist.size() <= 0
						&& !StringUtils.isEmpty(response.getLogined())
						&& response.getLogined().equals("0")) {
					mContent.findViewById(R.id.listView).setVisibility(
							View.GONE);
					mContent.findViewById(R.id.rl_empty).setVisibility(
							View.VISIBLE);
				} else {
					mContent.findViewById(R.id.rl_empty).setVisibility(
							View.GONE);
					mContent.findViewById(R.id.listView).setVisibility(
							View.VISIBLE);
				}
			} else {
				hasMore = false;
			}
	}
			catch (Exception e) {
			hasMore = false;
			e.printStackTrace();
		} finally {
			mProgressBar.setVisibility(View.GONE);
			mAdapter.notifyDataSetChanged();
			isRundata = false;
		}

	}

	@Override
	public void gainMyRewardfail() {
		ToastUtil.showToast(GoldenTabItemFragment.this.getActivity(),
				getResources().getString(R.string.net_error));
		mProgressBar.setVisibility(View.GONE);
		isRundata = false;
	}
}