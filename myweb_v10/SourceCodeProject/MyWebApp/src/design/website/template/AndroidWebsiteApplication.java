package design.website.template;

import android.app.Application;
import android.content.Context;
import design.website.template.util.AppConstants;
import design.website.template.util.AppUtils;

/**
 * This class is a application entry level class .
 * 
 * @author vishalbodkhe
 * 
 */
public class AndroidWebsiteApplication extends Application {

	/** Application context */
	private static Context mContext;
	public static boolean isTablet;
	private static String mAppUrl;

	@Override
	public void onCreate() {
		super.onCreate();
		setContext(getApplicationContext());
		isTablet = AppUtils.isTabletDevice(getApplicationContext());
		mAppUrl = AppConstants.PLAYSTORE_URL + mContext.getPackageName();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	/**
	 * @return the mAppUrl
	 */
	public static String getAppUrl() {
		return mAppUrl;
	}

	/**
	 * This method used to set the context.
	 * 
	 * @param context
	 */
	private static void setContext(Context context) {
		mContext = context;
	}

	/**
	 * This method used to get the context.
	 * 
	 * @return mContext
	 */
	public static Context getAppContext() {
		return mContext;
	}

}
