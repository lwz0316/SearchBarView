/*******************************************************************************
* Copyright 2014, 2015 Liu Wenzhu
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
 *******************************************************************************/
package com.lwz.searchbar.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 索引适配器
 * @author Liu Wenzhu
 */
public abstract class IndexAdapter<T extends IndexAdapter.Index> extends BaseAdapter {
	
	public static enum ViewType {
		index, normal
	}
	
	public static interface Index {
		public String getIndex();
	} 
	
	ArrayList<T> mOriginData;
	ArrayList<Object> mData;
	HashMap<String, Integer> mIndexPosMap;
	LayoutInflater mInflater;
	int mIndexLayoutRes;
	int mNormalLayoutRes;
	
	public IndexAdapter(Context context, int indexLayoutRes, int normalLayoutRes) {
		mInflater = LayoutInflater.from(context);
		mOriginData = new ArrayList<T>();
		mData = new ArrayList<Object>();
		mIndexPosMap = new HashMap<String, Integer>();
		mIndexLayoutRes = indexLayoutRes;
		mNormalLayoutRes = normalLayoutRes;
		clear();
	}
	
	public IndexAdapter(Context context, int indexLayoutRes, int normalLayoutRes, ArrayList<T> data) {
		this(context, indexLayoutRes, normalLayoutRes);
		updateData(data);
	}
	
	public void clear() {
		updateData(null);
	}
	
	public void updateData(Collection<? extends T> data) {
		mOriginData.clear();
		mData.clear();
		appendData(data);
	}
	
	public void appendData(Collection<? extends T> data) {
		if( data != null ) {
			mOriginData.addAll(data);
			IndexWrapperCreator.build((ArrayList<? extends Index>) data, mData, mIndexPosMap);
		}
		notifyDataSetChanged();
	}
	
	public ArrayList<Object> getData() {
		return mData;
	}
	
	public ArrayList<T> getOriginData() {
		return mOriginData;
	}
	
	/**
	 * 得到"索引"标识在数据源中的位置
	 * @param index "索引"
	 * @return -1 如果数据源中没有该"索引", 否则返回在数据源中的位置
	 */
	public int getIndexPosition(String index) {
		Integer res = mIndexPosMap.get(index);
		return res == null ? -1 : res;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return ViewType.values().length;
	}
	
	@Override
	public int getItemViewType(int position) {
		return getItemType(position).ordinal();
	}
	
	public ViewType getItemType(int position) {
		return mIndexPosMap.containsValue(position) ? ViewType.index : ViewType.normal;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewType viewType = getItemType(position);
		if( convertView == null ) {
			switch( viewType ) {
				case index :
					convertView = mInflater.inflate(mIndexLayoutRes, parent, false);
					break;
				default :
					convertView = mInflater.inflate(mNormalLayoutRes, parent, false);
			}
		}
		bindData(position, convertView, viewType, getItem(position));
		return convertView;
	}

	public abstract void bindData(int position, View convertView, ViewType viewType, Object itemData);
	
	// ===========
	// some util method
	// ===========
	public TextView bindText(View convertView, int textViewId, CharSequence value) {
		TextView textView = (TextView)getViewFromHolder(convertView, textViewId);
		textView.setText(value);
		return textView;
	}

	public <K extends View> K getViewFromHolder(View convertView, int id) {
		return ViewHolder.getView(convertView, id);
	}
	
	static class IndexWrapperCreator {
		
		private static TreeMap<String, ArrayList<Object>> tempMaps = 
				new TreeMap<String, ArrayList<Object>>(new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				return lhs.compareToIgnoreCase(rhs);
			}
		});
		
		public static void build(ArrayList<? extends Index> data, 
				ArrayList<Object> outData, HashMap<String, Integer> outIndexPosMap) {
			tempMaps.clear();
			if( data == null || data.isEmpty() ) {
				return;
			}
			// 初始化已有的数据源
			initTempMap(outData);
			outData.clear();
			outIndexPosMap.clear();
			
			// 将数据按组分块
			for( int i=0, count=data.size(); i<count; i++ ) {
				Index item = data.get(i);
				String index = item.getIndex();
				ArrayList<Object> group = tempMaps.get(index);
				if( group == null ) {
					group = new ArrayList<Object>();
					tempMaps.put(index, group);
				}
				group.add(item);
			}
			
			// 开始在组之间插入 index 数据， 并将结果设置到 out* 参数中
			Set<Entry<String, ArrayList<Object>>> entrySet = tempMaps.entrySet();
			for( Entry<String, ArrayList<Object>> entry : entrySet ) {
				ArrayList<Object> group = entry.getValue();
				if( !group.isEmpty() ) {
					outIndexPosMap.put(entry.getKey(), outData.size());
					outData.add(entry.getKey());
					outData.addAll(group);
				}
			}
			tempMaps.clear();
		}
		
		private static void initTempMap(ArrayList<Object> data) {
			for( int i=0, count=data.size(); i<count; i++ ) {
				Object item = data.get(i);
				if( item instanceof String ) {
					tempMaps.put((String)item, new ArrayList<Object>());
				} else {
					Index index = (Index) item;
					tempMaps.get(index.getIndex()).add(index);
				}
			}
		}
	}
	
	public static class OnItemClickListenerWrapper implements OnItemClickListener {

		OnItemClickListener listener;
		
		public OnItemClickListenerWrapper(OnItemClickListener l) {
			listener = l;
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if( listener != null ) {
				// 屏蔽 Index 的点击事件，只响应正常 item 的点击事件
				Object adapter = parent.getAdapter();
				if( adapter instanceof IndexAdapter<?> ) {
					IndexAdapter<?> indexAdapter = (IndexAdapter<?>) adapter;
					if( indexAdapter.getItemType(position) == ViewType.normal) {
						listener.onItemClick(parent, view, position, id);
					}
				}
			}
		}
		
	}

}
