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
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * ViewHolder 适配器，内部已经实现  {@link ViewHolder} 的逻辑，子类只需要简单实现抽象方法即可
 * @param <T>
 * 
 * @author Liu Wenzhu
 *
 */
public abstract class AbsViewHolderAdapter<T> extends BaseAdapter {
	
	Context mContext;
	List<T> mData;
	int mLayoutRes;
	View mCurrentConvertView;
	LayoutInflater mLayoutInflater;
	
	/**
	 * 使用该构造方法必须使用  {@link #update(Collection)} 或者 {@link #append(Collection)} 方法来更新数据
	 * @param context
	 * @param layoutRes
	 */
	public AbsViewHolderAdapter(Context context, int layoutRes) {
		this(context, new ArrayList<T>(), layoutRes);
	}
	
	public AbsViewHolderAdapter(Context context, List<T> data, int layoutRes) {
		mContext = context;
		mData = data;
		mLayoutRes = layoutRes;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public List<T> getData() {
		return mData;
	}
	
	/**
	 * 用新数据替换所有的旧数据
	 * <p>NOTE:数据源的指向并没有改变, 只是将数据源的数据{@link List #clear()}再 {@link List #addAll(Collection)}
	 * @param newData
	 */
	public synchronized void update(Collection<? extends T> newData) {
		mData.clear();
		if( newData != null ) {
			mData.addAll(newData);
		}
		notifyDataSetChanged();
	}
	
	/**
	 * 直接将源数据替换，将新数据的指向设置给适配器
	 * @param newData
	 */
	public void replaceOriginData(List<T> newData) {
		mData = (List<T>) newData;
		notifyDataSetChanged();
	}
	
	
	/**
	 * 在原有数据的基础上再添加数据
	 * <p>NOTE:数据源的指向并没有改变，只是在原有数据源的基础上添加数据
	 * @param appendData
	 */
	public synchronized void append(Collection<? extends T> appendData) {
		if( appendData == null || appendData.isEmpty() ) {
			return;
		}
		mData.addAll(appendData);
		notifyDataSetChanged();
	}
	
	/**
	 * 添加一个数据
	 * @param item
	 */
	public synchronized void add(T item) {
		mData.add(item);
		notifyDataSetChanged();
	}
	
	/**
	 * 清空所有数据
	 */
	public void clear() {
		mData.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ) {
			convertView = mLayoutInflater.inflate(mLayoutRes, parent, false);
		}
		mCurrentConvertView = convertView;
		bindData(position, getItem(position));
		
		return convertView;
	}
	
	abstract protected void bindData(int pos, T itemData);
	
	// ===========
	// some util method
	// ===========
	public void bindText(int textViewId, CharSequence value) {
		((TextView)getViewFromHolder(textViewId)).setText(value);
	}

	public <K extends View> K getViewFromHolder(int id) {
		return ViewHolder.getView(mCurrentConvertView, id);
	}
	
}
