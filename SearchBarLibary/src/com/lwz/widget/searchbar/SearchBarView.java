/*******************************************************************************
 * Copyright 2014, 2015 Liu Wenzhu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.lwz.widget.searchbar;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lwz.widget.R;

public class SearchBarView extends LinearLayout {

	EditText mSearchEdit;
	Button mCancelBtn;
	InputMethodManager mIMMService;
	
	public SearchBarView(Context context) {
		this(context, null);
	}
	
	public SearchBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mIMMService = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inflate(context, R.layout.widget_searchbar_edit, this);
		mSearchEdit = (EditText) findViewById(R.id.search_edit);
		mCancelBtn = (Button) findViewById(R.id.cancel_btn);
		mCancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onCancelAction();
			}
		});
		mSearchEdit.addTextChangedListener(mStateChangedListenerWrapper);
		// 将输入法右下角的按钮变为搜索按钮
		mSearchEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		mSearchEdit.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if( actionId == EditorInfo.IME_ACTION_SEARCH ){
					return mStateChangedListenerWrapper.onSearchEvent(mSearchEdit);
				}
				return false;
			}
		});
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 由于 mSearchEdit 被设置为不可用的 setEnable(false)，所以设置 click 监听也无效果,
		// 所以重写改方法来监听用户点击整个 searchbar 控件的事件，来将 mSearchEdit 设置为可以编辑的
		int action = ev.getAction();
		if( action == MotionEvent.ACTION_DOWN ) {
			if( !mSearchEdit.isEnabled() ) {
				onSearchAction();
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	
	/**
	 * 搜索动作方法，执行搜索操作时调用改方法
	 */
	private void onSearchAction() {
		mStateChangedListenerWrapper.onPrepareSearch(mSearchEdit);
		// 显示 取消按钮
		mCancelBtn.setVisibility(View.VISIBLE);
		
		// 改变search editText 的 gravity 和 宽度，使得 editText 靠左 
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSearchEdit.getLayoutParams();
		lp.gravity = Gravity.LEFT;
		lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
		mSearchEdit.setLayoutParams(lp);
		// 设置为可编辑的
		mSearchEdit.setEnabled(true);
		mSearchEdit.requestFocus();
		
		// 唤起输入法
		mIMMService.showSoftInput(
				mSearchEdit, InputMethodManager.SHOW_IMPLICIT);
	}
	
	/**
	 * 取消动作方法，当点击取消按钮时，调用该方法
	 */
	private void onCancelAction() {
		// 隐藏 取消按钮
		mCancelBtn.setVisibility(View.GONE);
		// 清空输入框中的内容
		mSearchEdit.setText(null);
		
		// 改变search editText 的 gravity 和 宽度, 使得 editText 居中
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSearchEdit.getLayoutParams();
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		lp.width = FrameLayout.LayoutParams.WRAP_CONTENT;
		mSearchEdit.setLayoutParams(lp);
		// 设置为不可编辑
		mSearchEdit.setEnabled(false);
		
		// 隐藏输入法
		mIMMService.hideSoftInputFromWindow(
				mSearchEdit.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
		mStateChangedListenerWrapper.onCancel(mSearchEdit);
	}
	
	public void setOnSearchBarStateChnagedListener(OnSearchBarStateChangedListener l) {
		mStateChangedListener = l;
	}
	
	public static interface OnSearchBarStateChangedListener extends TextWatcher {
		/**
		 * 准备搜索的时候会调用改方法
		 * @param searchEidt
		 */
		public void onPrepareSearch(EditText searchEdit);
		
		/**
		 * 搜索被取消的时候调用改方法
		 * @param searchEidt
		 */
		public void onCancel(EditText searchEdit);
		
		/**
		 * 用户点击输入法中的搜索按钮
		 * @param searchEdit
		 * 
		 * @return true if search event was handled, and IME was not dismiss.
		 * 		   false otherwise
		 */
		public boolean onSearchEvent(EditText searchEdit);
	}
	
	OnSearchBarStateChangedListener mStateChangedListener;
	OnSearchBarStateChangedListener mStateChangedListenerWrapper = 
			new OnSearchBarStateChangedListener() {

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					if( mStateChangedListener != null ) {
						mStateChangedListener.beforeTextChanged(s, start, count, after);
					}
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if( mStateChangedListener != null ) {
						mStateChangedListener.onTextChanged(s, start, before, count);
					}
				}

				@Override
				public void afterTextChanged(Editable s) {
					if( mStateChangedListener != null ) {
						mStateChangedListener.afterTextChanged(s);
					}
				}

				@Override
				public void onPrepareSearch(EditText searchEdit) {
					if( mStateChangedListener != null ) {
						mStateChangedListener.onPrepareSearch(searchEdit);
					}
				}

				@Override
				public void onCancel(EditText searchEdit) {
					if( mStateChangedListener != null ) {
						mStateChangedListener.onCancel(searchEdit);
					}
				}

				@Override
				public boolean onSearchEvent(EditText searchEdit) {
					if( mStateChangedListener != null ) {
						return mStateChangedListener.onSearchEvent(searchEdit);
					}
					return false;
				}
		
	};
	
	public static class SimpleSearchBarStateChangedListener implements OnSearchBarStateChangedListener {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void onPrepareSearch(EditText searchEdit) {
		}

		@Override
		public void onCancel(EditText searchEdit) {
		}

		@Override
		public boolean onSearchEvent(EditText searchEdit) {
			return false;
		}
		
	}
	
}
