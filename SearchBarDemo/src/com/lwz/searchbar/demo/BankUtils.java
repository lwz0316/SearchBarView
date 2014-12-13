package com.lwz.searchbar.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BankUtils {

	public static ArrayList<Bank> loadHotBank(AssetManager assetManager) {
		ArrayList<Bank> hotBanks = null;
		try {
			InputStream is = assetManager.open("bank/hot_bank.json");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int offset;
			while( (offset = is.read(buffer)) != -1 ) {
				baos.write(buffer, 0, offset);
			}
			is.close();
			baos.flush();
			hotBanks = new Gson().fromJson(baos.toString(), new TypeToken<ArrayList<Bank>>(){}.getType());
			is.close();
			baos.close();
		} catch (IOException e) {
		}
		return hotBanks == null ? new ArrayList<Bank>() : hotBanks;
	}
	
	static HashMap<String, SoftReference<Drawable>> mLogoCacheMap =
			new HashMap<String, SoftReference<Drawable>>(); 
	
	public static Drawable getBankLogo(Context context, String logoName) {
		SoftReference<Drawable> logoRef = mLogoCacheMap.get(logoName);
		Drawable logo = null;
		if( logoRef == null || (logo = logoRef.get()) == null ) {
			logo = BankUtils.getAsssetDrawableByName(
					context, BankUtils.getBankLogoPath(logoName));
			mLogoCacheMap.put(logoName, new SoftReference<Drawable>(logo));
		}
		return logo;
	}
	
	public static String getBankLogoPath(String logoName) {
		return String.format("bank/%s.png", logoName);
	}
	
	public static Drawable getAsssetDrawableByName(Context context, String name) {
		try {
			InputStream is = context.getAssets().open(name);
			return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(is));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ColorDrawable(Color.TRANSPARENT);
	}
}
