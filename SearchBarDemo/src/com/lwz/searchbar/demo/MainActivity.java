package com.lwz.searchbar.demo;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lwz.letterbarview.lib.LetterBarView;
import com.lwz.letterbarview.lib.LetterBarView.OnLetterSelectListener;
import com.lwz.searchbar.demo.BankFilter.OnPublishFilterResultsListener;
import com.lwz.searchbar.demo.IndexAdapter.OnItemClickListenerWrapper;
import com.lwz.widget.searchbar.SearchBarView;
import com.lwz.widget.searchbar.SearchBarView.SimpleSearchBarStateChangedListener;

public class MainActivity extends FragmentActivity implements
		OnLetterSelectListener, OnItemClickListener,
		OnPublishFilterResultsListener {

	public static final String[] LETTERS = { "热", "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	public static final String PROPERTY_ON_ITEM_CLICK = "onItemClick";
	public static final String PROPERTY_BANK_DATA = "bank_data";

	LetterBarView mLetterBarView;
	ListView mBankList;
	BankIndexAdapter mBankIndexAdapter;

	SearchBarView mSearchBar;
	ViewGroup mSearchResultContainer;
	ListView mSearchResultList; // 搜索结果 list
	BankSearchResultAdapter mSearchResultAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		mSearchBar = (SearchBarView) findViewById(R.id.searchbar);
		mLetterBarView = (LetterBarView) findViewById(R.id.letterbar);
		mLetterBarView.setOverlayOffset(0, -200);
		mBankList = (ListView) findViewById(R.id.bank_list);
		mBankIndexAdapter = new BankIndexAdapter(this);
		mBankIndexAdapter.setOnPublishFilterResultsListener(this);
		mBankList.setAdapter(mBankIndexAdapter);
		mBankList.setOnItemClickListener(new OnItemClickListenerWrapper(this));

		mLetterBarView.setLetterSet(LETTERS);

		mSearchResultContainer = (ViewGroup) findViewById(R.id.search_result_container);
		mSearchResultList = (ListView) findViewById(R.id.search_result_list);
		mSearchResultAdapter = new BankSearchResultAdapter(this);
		mSearchResultList.setAdapter(mSearchResultAdapter);
		mSearchResultList.setOnItemClickListener(this);

		mLetterBarView.setOnLetterSelectListener(this);
		mSearchBar.setOnSearchBarStateChnagedListener(
				new SimpleSearchBarStateChangedListener() {

					@Override
					public void onPrepareSearch(EditText searchEidt) {
						showSearchResultContainer();
					}

					@Override
					public void onCancel(EditText searchEidt) {
						dismissSearchResultContainer();
					}

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						onSearchContentChanged(s.toString());
					}

				});

		// 加载热门银行数据，相关数据在 asset/bank 目录下
		mBankIndexAdapter.updateData(BankUtils.loadHotBank(getAssets()));
	}

	@Override
	public void onLetterSelect(String s) {
		if (!s.equals("热")) {
			mBankList.setSelection(mBankIndexAdapter.getIndexPosition(s));
		} else {
			mBankList.setSelection(0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Object itemData = parent.getItemAtPosition(position);
		if (itemData instanceof Bank) {
			Toast.makeText(this, ((Bank) itemData).name, Toast.LENGTH_SHORT).show();
		}
	}

	private void showSearchResultContainer() {
		mSearchResultContainer.setVisibility(View.VISIBLE);
		// 禁止点击事件向下传递
		mSearchResultContainer.setClickable(true);
	}

	private void dismissSearchResultContainer() {
		mSearchResultContainer.setVisibility(View.INVISIBLE);
	}

	private void onSearchContentChanged(String text) {
		if (TextUtils.isEmpty(text)) {
			mSearchResultAdapter.clear();
			mSearchResultList.setBackgroundColor(Color.parseColor("#77000000"));
			return;
		}
		mSearchResultList.setBackgroundColor(Color.WHITE);
		mBankIndexAdapter.filter(text);
	}

	@Override
	public void onPublishResults(CharSequence constraint,
			ArrayList<Bank> results) {
		mSearchResultAdapter.update(results);
	}

	public void setBankData(ArrayList<Bank> data) {
		mBankIndexAdapter.appendData(data);
	}
}
