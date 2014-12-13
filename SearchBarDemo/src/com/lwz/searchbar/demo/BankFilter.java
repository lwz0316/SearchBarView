package com.lwz.searchbar.demo;

import java.util.ArrayList;

import android.text.TextUtils;
import android.widget.Filter;

public class BankFilter extends Filter {
	
	public static interface OnPublishFilterResultsListener {
		public void onPublishResults(CharSequence constraint, ArrayList<Bank> results);
	}
	
	ArrayList<Bank> mData;
	OnPublishFilterResultsListener mOnPublishFilterResultsListener;
	
	public BankFilter(ArrayList<Bank> data) {
		this.mData = data;
	}
	
	public void setOnPublishFilterResultsListener(OnPublishFilterResultsListener l) {
		mOnPublishFilterResultsListener = l;
	}
	
	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		FilterResults results = new FilterResults();
		ArrayList<Bank> result = new ArrayList<Bank>();
		if( !TextUtils.isEmpty(constraint) ) {
			for (int i=0, count=mData.size(); i<count; i++) {  
				Bank item = mData.get(i);
                if ( item.contains(constraint) && !result.contains(item) ) {
                	result.add(item);
                }  
            }
		}
		results.values = result;
		results.count = result.size();
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		if( mOnPublishFilterResultsListener != null ) {
			mOnPublishFilterResultsListener.onPublishResults(constraint, (ArrayList<Bank>)results.values);
		}
	}
	
}
