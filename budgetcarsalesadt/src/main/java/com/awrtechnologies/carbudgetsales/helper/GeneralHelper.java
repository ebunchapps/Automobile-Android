package com.awrtechnologies.carbudgetsales.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.WindowManager;

public class GeneralHelper {

	private static GeneralHelper generalHelper;
	private Context context;
	private boolean ischeck;
	private boolean ischeckimage;
	private boolean ischeckdonetime;
	private Fragment tempFragment;
	private boolean ischeckfragment;
	private boolean ischeckreceipt;

	private boolean ischeckSameCarid;

	public static synchronized GeneralHelper getInstance(Context context) {
		if (generalHelper == null) {
			generalHelper = new GeneralHelper(context);
		}
		return generalHelper;
	}

	public GeneralHelper(Context context) {
		this.context = context;
	}

	/**
	 * Convert dp in pixels
	 * 
	 * @param dp
	 * @return
	 */
	public int getPx(int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return ((int) (dp * scale + 0.5f));
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public Point getScreenSize() {

		Point size = new Point();
		WindowManager w = ((Activity) context).getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			w.getDefaultDisplay().getSize(size);
		} else {
			Display d = w.getDefaultDisplay();
			size.x = d.getWidth();
			size.y = d.getHeight();
		}
		return size;
	}

	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}

	public boolean isIscheckimage() {
		return ischeckimage;
	}

	public void setIscheckimage(boolean ischeckimage) {
		this.ischeckimage = ischeckimage;
	}

	public boolean isIscheckdonetime() {
		return ischeckdonetime;
	}

	public void setIscheckdonetime(boolean ischeckdonetime) {
		this.ischeckdonetime = ischeckdonetime;
	}

	public Fragment getTempFragment() {
		return tempFragment;
	}

	public void setTempFragment(Fragment tempFragment) {
		this.tempFragment = tempFragment;
	}

	public boolean isIscheckfragment() {
		return ischeckfragment;
	}

	public void setIscheckfragment(boolean ischeckfragment) {
		this.ischeckfragment = ischeckfragment;
	}

	public static GeneralHelper getGeneralHelper() {
		return generalHelper;
	}

	public static void setGeneralHelper(GeneralHelper generalHelper) {
		GeneralHelper.generalHelper = generalHelper;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public boolean isIscheckreceipt() {
		return ischeckreceipt;
	}

	public void setIscheckreceipt(boolean ischeckreceipt) {
		this.ischeckreceipt = ischeckreceipt;
	}


	public boolean ischeckSameCarid() {
		return ischeckSameCarid;
	}

	public void setIscheckSameCarid(boolean ischeckSameCarid) {
		this.ischeckSameCarid = ischeckSameCarid;
	}
}
