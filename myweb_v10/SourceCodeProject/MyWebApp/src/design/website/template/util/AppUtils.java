/**
 * 
 */
package design.website.template.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import design.website.template.R;
import design.website.template.config.AppConfig;

/**
 * The class to store utility methods to used in whole application
 * 
 * @author vishalbodkhe
 * 
 */
public class AppUtils {

	private static LayoutInflater inflater;
	private static TextView toastTextview;
	private static Toast toast;
	private static View toastView;

	/**
	 * Method to add and show ads banner view
	 * 
	 * @param context
	 * @param view
	 */
	public static void addAdsBannerView(Activity context, RelativeLayout view) {
		if (isInternetConnected(context)) {
			AdRequest adRequest = new AdRequest.Builder().build();
			AdView name = new AdView((Activity) context);
			name.setAdSize(AdSize.BANNER);
			name.setAdUnitId(AppConfig.ADMOB_ADS_ID);
			name.loadAd(adRequest);
			view.removeAllViews();
			view.addView(name);
		} else {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * Method to show custom toast message
	 * 
	 * @param context
	 * @param message
	 */
	public static void showToast(Context context, String message) {
		if (inflater == null || toastView == null) {
			inflater = (LayoutInflater) context.getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			toastView = inflater.inflate(R.layout.toast_layout, null);
			toastTextview = (TextView) toastView.findViewById(R.id.text);
			toast = new Toast(context);
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toastTextview.setText(message);
		toast.setView(toastView);
		toast.show();
	}

	/**
	 * This method used to get the result that the given device is tablet or not
	 * on basis of screen layout.
	 * 
	 * @param context
	 * @return result
	 */
	public static boolean isTabletDevice(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * Method to check internet
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isInternetConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		if (info.getState() != State.CONNECTED) {
			return false;
		}
		return true;
	}

}
