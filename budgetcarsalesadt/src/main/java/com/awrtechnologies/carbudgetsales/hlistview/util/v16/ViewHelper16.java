package com.awrtechnologies.carbudgetsales.hlistview.util.v16;


import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import com.awrtechnologies.carbudgetsales.hlistview.util.v14.ViewHelper14;

public class ViewHelper16 extends ViewHelper14 {
	public ViewHelper16( View view ) {
		super( view );
	}

	
	@TargetApi( Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void postOnAnimation( Runnable action ) {
		view.postOnAnimation(action);
	}
}