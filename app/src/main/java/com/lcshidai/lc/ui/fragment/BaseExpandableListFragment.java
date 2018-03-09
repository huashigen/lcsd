package com.lcshidai.lc.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.lcshidai.lc.R;
import com.lcshidai.lc.ui.base.TRJFragment;

public class BaseExpandableListFragment extends TRJFragment {
	protected View mProgressContainer;
	protected ExpandableListView mListView;
	protected ExpandableListAdapter mAdapter;
	protected View mEmptyView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_expandable_list, container, false);
		mListView = (ExpandableListView) contentView.findViewById(R.id.listview);
		mListView.setGroupIndicator(null);
		
		mEmptyView = contentView.findViewById(R.id.empty);
		mListView.setEmptyView(mEmptyView);

		mProgressContainer = contentView.findViewById(R.id.progressContainer);
		return contentView;
	}

	public void setAdapter(ExpandableListAdapter adapter) {
		mAdapter = adapter;
		mListView.setAdapter(mAdapter);
		mProgressContainer.setVisibility(View.GONE);
	}

	public abstract class ExpandableListAdapter<K, V> extends BaseExpandableListAdapter {
		private HashMap<K, ArrayList<V>> mMap = new HashMap<K, ArrayList<V>>();
		private ArrayList<K> mArrayList = new ArrayList<K>();

		public void add(K k, ArrayList<V> a) {
			mArrayList.add(k);
			mMap.put(k, a);
		}

		@Override
		public int getGroupCount() {
			return mArrayList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return mMap.get(mArrayList.get(groupPosition)).size();
		}

		@Override
		public K getGroup(int groupPosition) {
			return mArrayList.get(groupPosition);
		}

		@Override
		public V getChild(int groupPosition, int childPosition) {
			return mMap.get(getGroup(groupPosition)).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return groupPosition * 10000 + childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public abstract View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

		@Override
		public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}
}

