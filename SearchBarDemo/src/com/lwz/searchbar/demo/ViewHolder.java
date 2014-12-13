package com.lwz.searchbar.demo;

import android.util.SparseArray;
import android.view.View;

/**
 * 适配器中的 ViewHolder 类
 * 
 * @link 参考：http://www.piwai.info/android-adapter-good-practices/#Update
 * @author Liu Wenzhu
 * @mail liu.wenzhu@rytong.com
 *
 * Copyright ® 2014 北京融易通信息技术有限公司
 * 版权所有。
 *
 */
public class ViewHolder {
	
	@SuppressWarnings("unchecked")
	public static <T extends View> T getView(View convertView, int id) {
		
		SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
		if( holder == null ) {
			holder = new SparseArray<View>();
			convertView.setTag(holder);
		}
		
		View view = holder.get(id);
		if( view == null ) {
			view = convertView.findViewById(id);
			holder.put(id, view);
		}
		return (T)view;
	}
}