package com.lwz.searchbar.demo;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lwz.searchbar.demo.BankFilter.OnPublishFilterResultsListener;

public class BankIndexAdapter extends IndexAdapter<Bank> {

	BankFilter mBankFilter;
	AssetManager mAssetManager;
	
	public BankIndexAdapter(Context context) {
		super(context, R.layout.item_list_bank_index, R.layout.item_list_bank);
		mBankFilter = new BankFilter(getOriginData());
		mAssetManager = context.getAssets();
	}

	@Override
	public void bindData(int position, View convertView, ViewType viewType, Object itemData) {
		if( viewType == ViewType.index ) {
			String key = String.valueOf(itemData);
			if( "#".equals(key)) {
				key = "热";
			}
			bindText(convertView, R.id.bank_index, key);
		} else {
			Bank bank = (Bank)itemData;
			bindText(convertView, R.id.bank_name, bank.name);
			ImageView bankLogo = getViewFromHolder(convertView, R.id.bank_logo);
			if( !TextUtils.isEmpty(bank.logoName) ) {
				bankLogo.setImageDrawable(BankUtils.getBankLogo(bankLogo.getContext(), bank.logoName));
			} else {
				bankLogo.setImageDrawable(null);
			}
		}
	}
	
	public void filter(CharSequence keyword) {
		mBankFilter.filter(keyword);
	}
	
	public void setOnPublishFilterResultsListener(OnPublishFilterResultsListener l) {
		mBankFilter.setOnPublishFilterResultsListener(l);
	}
	
}
