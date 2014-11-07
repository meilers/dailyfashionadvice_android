package com.sobremesa.waywt;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class DFAApplication extends Application {

	private static Context sContext;
	private static Handler sHandler;
    private static ImageLoader sImageLoader;


	@Override
	public void onCreate() {
		super.onCreate();

		sHandler = new Handler();
		sContext = getApplicationContext();

        Drawable placeHolder = getResources().getDrawable(R.drawable.ic_launcher);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                cacheInMemory(true).
                cacheOnDisk(true).
                showImageForEmptyUri(placeHolder).
                showImageOnFail(placeHolder).
                imageScaleType(ImageScaleType.EXACTLY).
                showImageOnLoading(placeHolder).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(DFAApplication.getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        sImageLoader = ImageLoader.getInstance();
        sImageLoader.init(config);

	}
	
	

	public static void runOnUiThread(Runnable runnable) {
		sHandler.post(runnable);
	}

	public static final Context getContext() {
		return sContext;
	}

	public static final ContentResolver getResolver() {
		return sContext.getContentResolver();
	}

    public static final ImageLoader getImageLoader() {
        return sImageLoader;
    }


    public static SharedPreferences getSharedPreferences() {
		SharedPreferences prefs = sContext.getSharedPreferences(sContext.getPackageName(), Context.MODE_PRIVATE);
		return prefs;
	}

	public static String getResourceString(int resource) {
		return sContext.getResources().getString(resource);
	}

}
