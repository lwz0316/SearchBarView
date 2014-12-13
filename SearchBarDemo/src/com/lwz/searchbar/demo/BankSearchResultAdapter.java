package com.lwz.searchbar.demo;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * 银行搜索结果适配器
 * @author Liu Wenzhu
 */
public class BankSearchResultAdapter extends AbsViewHolderAdapter<Bank> {

	AssetManager mAssetManager;
	
	public BankSearchResultAdapter(Context context) {
		super(context, R.layout.item_list_bank);
		mAssetManager = context.getAssets();
	}

	@Override
	protected void bindData(int pos, Bank itemData) {
		bindText(R.id.bank_name, itemData.name);
		Bank bank = (Bank)itemData;
		ImageView bankLogo = getViewFromHolder(R.id.bank_logo);
		if( !TextUtils.isEmpty(bank.logoName) ) {
			bankLogo.setImageDrawable(BankUtils.getBankLogo(bankLogo.getContext(), bank.logoName));
		} else {
			bankLogo.setImageDrawable(null);
		}
	}
	
	

}
