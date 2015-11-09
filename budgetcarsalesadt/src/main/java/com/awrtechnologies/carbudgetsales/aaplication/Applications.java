package com.awrtechnologies.carbudgetsales.aaplication;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;

import com.activeandroid.ActiveAndroid;
import com.awrtechnologies.carbudgetsales.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
//
//@ReportsCrashes(
//		mailTo = "chetan@awrtechnologies.com",
//		mode = ReportingInteractionMode.TOAST,
//		resToastText = R.string.crash_string)

public class Applications extends Application {

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}

	@Override
	public void onCreate() {

		//ACRA.init(this);
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()

		.resetViewBeforeLoading(false)
				// default
				.showImageOnLoading(R.drawable.loading).delayBeforeLoading(20)
				.cacheInMemory(false) // default
				.considerExifParams(false) // default
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new SimpleBitmapDisplayer()) // default
				.handler(new Handler()) // default
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()

				.memoryCache(new WeakMemoryCache()).threadPoolSize(3)
				// .discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.defaultDisplayImageOptions(options).build();

		ImageLoader.getInstance().init(config);
		ActiveAndroid.initialize(this);
		ActiveAndroid.setLoggingEnabled(true);
		super.onCreate();
	}

}
