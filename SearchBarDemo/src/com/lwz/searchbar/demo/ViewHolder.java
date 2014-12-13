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