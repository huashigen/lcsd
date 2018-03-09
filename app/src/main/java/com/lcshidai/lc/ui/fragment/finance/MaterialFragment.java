package com.lcshidai.lc.ui.fragment.finance;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.impl.finance.MaterialInfoImpl;
import com.lcshidai.lc.model.finance.MaterialInfoData;
import com.lcshidai.lc.model.finance.MaterialInfoListJson;
import com.lcshidai.lc.service.finance.HttpMaterialInfoService;
import com.lcshidai.lc.ui.base.TRJActivity;
import com.lcshidai.lc.ui.base.TRJFragment;
import com.lcshidai.lc.ui.finance.MaterialActivity;
import com.lcshidai.lc.utils.GoLoginUtil;
import com.lcshidai.lc.widget.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import com.lcshidai.lc.widget.touchgallery.GalleryWidget.UrlPagerAdapter;

/**
 * 
 * @author 001355
 * @Description: 项目信息-项目材料公示
 */
public class MaterialFragment extends TRJFragment implements MaterialInfoImpl {

	private ViewPager viewPager; // android-support-v4中的滑动组件
	UrlPagerAdapter madapter;
	public View mContentView;

	public String prj_id;

	TextView tv_content, tv_num_page;
	HttpMaterialInfoService hmis;
	int index = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			prj_id = args.getString("prj_id");
			index = args.getInt("index");
		}
		hmis = new HttpMaterialInfoService((MaterialActivity) getActivity(), this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.material_fragment, container,
				false);
		viewPager = (ViewPager) mContentView.findViewById(R.id.vp);
		tv_content = (TextView) mContentView.findViewById(R.id.tv_content);
		tv_num_page = (TextView) mContentView.findViewById(R.id.tv_num_page);
		return mContentView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}

	public void setView(final List<MaterialInfoData> itemList) {
		if (itemList.size() < 1) {
			mContentView.findViewById(R.id.rl_empty)
					.setVisibility(View.VISIBLE);
			viewPager.setVisibility(View.GONE);
			mContentView.findViewById(R.id.rl_bottom_c)
					.setVisibility(View.GONE);
			return;

		}
		mContentView.findViewById(R.id.rl_bottom_c).setVisibility(View.VISIBLE);
		viewPager.setVisibility(View.VISIBLE);
		mContentView.findViewById(R.id.rl_empty).setVisibility(View.GONE);
		List<String> items = new ArrayList<String>();
		for (MaterialInfoData it : itemList) {
			items.add(it.getBig());
		}
		madapter = new UrlPagerAdapter(getActivity(), items);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(madapter);
		// viewPager.setOnPageChangeListener(new OnPageChangeListener() {
		// boolean isDragging=false;
		// @Override
		// public void onPageSelected(int arg0) {
		// isDragging=false;
		// if(arg0==ViewPager.SCROLL_STATE_DRAGGING){
		// if(viewPager.getCurrentItem()==0){
		// isDragging=true;
		// }
		// }
		// }
		//
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		// if(arg0==0&&arg1==arg2&&isDragging){
		// }
		// }
		//
		// @Override
		// public void onPageScrollStateChanged(int arg0) {
		//
		// }
		// });
		tv_num_page.setText(1 + "/" + itemList.size());
		tv_content.setText(itemList.get(0).getName());
		madapter.setOnItemChangeListener(new OnItemChangeListener() {

			@Override
			public void onItemChange(int currentPosition) {
				tv_num_page.setText(currentPosition + 1 + "/" + itemList.size());
				tv_content.setText(itemList.get(currentPosition).getName());
			}
		});

		try {
			viewPager.setCurrentItem(index);
		} catch (Exception e) {
		}
	}

	public void loadData() {
		hmis.gainMaterialInfo(prj_id);

	}

	@Override
	public void gainMaterialInfosuccess(MaterialInfoListJson response) {
		try {
			if (response != null) {
				String boolen = response.getBoolen();
				if (boolen.equals("1")) {

					List<MaterialInfoData> list = response.getList();

					setView(list);
					// mAdapter.clear();
				} else {
					GoLoginUtil.ToLoginActivityForResultBase(
							(TRJActivity) getActivity(), 1000, "");
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	@Override
	public void gainMaterialInfofail() {

	}

}
