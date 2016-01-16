/**
 * 
 */
package design.website.template.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import design.website.template.BaseActivity;
import design.website.template.R;
import design.website.template.activity.AboutActivity.ActionBrodcastListener;
import design.website.template.config.AppConfig;
import design.website.template.manager.ThemeContentManager;
import design.website.template.model.ThemeItem;
import design.website.template.util.AppConstants;
import design.website.template.util.AppUtils;
import design.website.template.util.SettingsPreferences;

/**
 * The class content all settings preferences
 * 
 * @author vishalbodkhe
 * 
 */
public class SettingsActivity extends BaseActivity {

	private Context mContext;
	private ImageView mCacheImageview;
	private ImageView mJavascriptImageview;
	private ImageView mZoomControlImageview;
	private ImageView mScrollbarsImageview;
	private RelativeLayout mHeaderView;
	private boolean isCacheEnabled;
	private boolean isJavascriptEnabled;
	private boolean isScrollbarEnabled;
	private boolean isZoomEnabled;
	public InterstitialAd interstitial;
	private ActionBrodcastListener mActionBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		mContext = SettingsActivity.this;
		initViews();
		initInteristitialAds();
		setDefaultTheme(SettingsPreferences.getThemeIndex(mContext));
		if (savedInstanceState == null) {
			isCacheEnabled = SettingsPreferences.isCacheEnabled(mContext);
			isJavascriptEnabled = SettingsPreferences
					.isJavascriptEnabled(mContext);
			isScrollbarEnabled = SettingsPreferences
					.isScrollbarEnabled(mContext);
			isZoomEnabled = SettingsPreferences.isZoomEnabled(mContext);
		} else {
			isCacheEnabled = savedInstanceState
					.getBoolean(AppConstants.EXTRA_CACHE_KEY);
			isJavascriptEnabled = savedInstanceState
					.getBoolean(AppConstants.EXTRA_JAVASCRIPT_KEY);
			isScrollbarEnabled = savedInstanceState
					.getBoolean(AppConstants.EXTRA_SCROLLBAR_KEY);
			isZoomEnabled = savedInstanceState
					.getBoolean(AppConstants.EXTRA_ZOOM_KEY);
		}
		updateCacheStatus(isCacheEnabled);
		updateJavascriptStatus(isJavascriptEnabled);
		updateScrollbarStatus(isScrollbarEnabled);
		updateZoomStatus(isZoomEnabled);
	}

	@SuppressWarnings("unused")
	private void initInteristitialAds() {
		if (AppUtils.isInternetConnected(mContext)
				&& AppConfig.INTERSTITIAL_ADS_ENABLED) {
			interstitial = new InterstitialAd(this);
			interstitial.setAdUnitId(AppConfig.ADMOB_ADS_ID);
			AdRequest adRequest = new AdRequest.Builder().build();
			interstitial.loadAd(adRequest);

			interstitial.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					if (interstitial.isLoaded()) {
						interstitial.show();
					}
				}
			});
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(AppConstants.EXTRA_CACHE_KEY, isCacheEnabled);
		outState.putBoolean(AppConstants.EXTRA_JAVASCRIPT_KEY,
				isJavascriptEnabled);
		outState.putBoolean(AppConstants.EXTRA_SCROLLBAR_KEY,
				isScrollbarEnabled);
		outState.putBoolean(AppConstants.EXTRA_ZOOM_KEY, isZoomEnabled);
	}

	/**
	 * Method to show inits component
	 */
	private void initViews() {
		mCacheImageview = (ImageView) findViewById(R.id.cache_imageview);
		mJavascriptImageview = (ImageView) findViewById(R.id.javascript_imageview);
		mScrollbarsImageview = (ImageView) findViewById(R.id.scrollbars_imageview);
		mZoomControlImageview = (ImageView) findViewById(R.id.zoom_control_imageview);
		mActionBroadcastReceiver = new ActionBrodcastListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstants.UPDATE_BACKGROUND_THEME);
		registerReceiver(mActionBroadcastReceiver, intentFilter);
	}

	/**
	 * Method to update the cache status.
	 * 
	 * @param status
	 */
	private void updateCacheStatus(boolean status) {
		if (status) {
			mCacheImageview.setBackgroundResource(R.drawable.cb_checked);
		} else {
			mCacheImageview.setBackgroundResource(R.drawable.cb_unchecked);
		}
	}

	/**
	 * Method to update the javascript status.
	 * 
	 * @param status
	 */
	private void updateJavascriptStatus(boolean status) {
		if (status) {
			mJavascriptImageview.setBackgroundResource(R.drawable.cb_checked);
		} else {
			mJavascriptImageview.setBackgroundResource(R.drawable.cb_unchecked);
		}
	}

	/**
	 * Method to update the scrollbar status.
	 * 
	 * @param status
	 */
	private void updateScrollbarStatus(boolean status) {
		if (status) {
			mScrollbarsImageview.setBackgroundResource(R.drawable.cb_checked);
		} else {
			mScrollbarsImageview.setBackgroundResource(R.drawable.cb_unchecked);
		}
	}

	/**
	 * Method to update the zoom status.
	 * 
	 * @param status
	 */
	private void updateZoomStatus(boolean status) {
		if (status) {
			mZoomControlImageview.setBackgroundResource(R.drawable.cb_checked);
		} else {
			mZoomControlImageview
					.setBackgroundResource(R.drawable.cb_unchecked);
		}
	}

	/**
	 * Method to get click actions on registered views.
	 * 
	 * @param view
	 */
	public void viewClickHandler(View view) {
		switch (view.getId()) {
		case R.id.back_view:
			finish();
			break;
		case R.id.ok_view:
			saveSettings();
			finish();
			break;
		case R.id.cache_parent_view:
			isCacheEnabled = !isCacheEnabled;
			updateCacheStatus(isCacheEnabled);
			break;
		case R.id.javascript_parent_view:
			isJavascriptEnabled = !isJavascriptEnabled;
			updateJavascriptStatus(isJavascriptEnabled);
			break;
		case R.id.scrollbars_parent_view:
			isScrollbarEnabled = !isScrollbarEnabled;
			updateScrollbarStatus(isScrollbarEnabled);
			break;
		case R.id.zoom_control_parent_view:
			isZoomEnabled = !isZoomEnabled;
			updateZoomStatus(isZoomEnabled);
			break;
		}

	}

	/**
	 * This method is used to send broadcast
	 */
	private void saveSettings() {
		SettingsPreferences.setCacheEnabled(mContext, isCacheEnabled);
		SettingsPreferences.setJavascriptEnabled(mContext, isJavascriptEnabled);
		SettingsPreferences.setScrollbarEnabled(mContext, isScrollbarEnabled);
		SettingsPreferences.setZoomEnabled(mContext, isZoomEnabled);
		Intent intent = new Intent();
		intent.setAction(AppConstants.SETTINGS_UPDATED);
		sendBroadcast(intent);
	}

	/**
	 * Method to set toolbar theme
	 * 
	 * @param position
	 */
	private void setDefaultTheme(int position) {
		if (mHeaderView == null) {
			mHeaderView = (RelativeLayout) findViewById(R.id.titlebar_layout);
		}
		mHeaderView.setBackgroundColor(ThemeContentManager.getInstance().getTheme(mContext));
	}

	/**
	 * The class is used to get broadcasted action for which action it is
	 * registered
	 * 
	 * @author vishalbodkhe
	 * 
	 */
	public class ActionBrodcastListener extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(AppConstants.UPDATE_BACKGROUND_THEME)) {
				setDefaultTheme(SettingsPreferences.getThemeIndex(mContext));
			}

		}
	}

	@Override
	protected void onDestroy() {
		if (mContext != null) {
			if (mActionBroadcastReceiver != null) {
				mContext.unregisterReceiver(mActionBroadcastReceiver);
				mActionBroadcastReceiver = null;
			}
			mHeaderView = null;
			mCacheImageview = null;
			mJavascriptImageview = null;
			mZoomControlImageview = null;
			mScrollbarsImageview = null;
			mContext = null;
			super.onDestroy();
		}
	}
}
