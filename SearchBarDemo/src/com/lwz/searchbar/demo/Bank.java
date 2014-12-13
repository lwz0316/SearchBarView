package com.lwz.searchbar.demo;

import java.io.Serializable;

import android.text.TextUtils;

import com.lwz.searchbar.demo.IndexAdapter.Index;

public class Bank implements Index, Serializable {

	private static final long serialVersionUID = -3279686078402222603L;

	private String key;
	public String name;
	private String code;
	public String logoName;
	private String mIndex;
	
	public Bank(String key, String name, String code) {
		this(key, name, code, null);
	}
	
	public Bank(String key, String name, String code, String logoName) {
		super();
		this.key = key;
		this.name = name;
		this.code = code;
		this.logoName = logoName;
	}
	
	public String getCode() {
		return TextUtils.isEmpty(code) ? "0" : code;
	}

	public String getKey() {
		return key;
	}
	
	public boolean contains(CharSequence key) {
		return name.contains(key);
	}

	@Override
	public String getIndex() {
		if( TextUtils.isEmpty(mIndex) ) {
			mIndex = getKey();
			if( TextUtils.isEmpty(mIndex) ) mIndex = "A";
			if( mIndex.length() > 1 ) {
				mIndex = mIndex.substring(0, 1);
			};
		}
		return mIndex;
	}

	@Override
	public String toString() {
		return "Bank [key=" + key + ", name=" + name + ", code=" + code
				+ ", logoName=" + logoName + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if( o instanceof Bank ) {
			return ((Bank) o).getCode().equalsIgnoreCase(getCode());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getCode().hashCode();
	}
	
}
