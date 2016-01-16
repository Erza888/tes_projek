/**
 * 
 */
package design.website.template.activity;

import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ZoomButtonsController;
import design.website.template.BaseActivity;
import design.website.template.R;
import design.website.template.config.AppConfig;
import design.website.template.lib.CircleProgressBar;
import design.website.template.manager.ThemeContentManager;
import design.website.template.util.AppConstants;
import design.website.template.util.AppUtils;
import design.website.template.util.SettingsPreferences;

/**
 * The activity to show Contact Us page
 * 
 * @author vishalbodkhe
 * 
 */
public class ContactUsActivity extends BaseActivity {

	private Context mContext;
	private RelativeLayout mHeaderView;
	private RelativeLayout mAdsView;
	private WebView mWebView;
	private CircleProgressBar mProgressBar;
	private Dialog mCustomDialog;
	private ActionBrodcastListener mActionBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_us_layout);
		mContext = ContactUsActivity.this;
		initViews();
		setDefaultTheme(SettingsPreferences.getThemeIndex(mContext));
		if (savedInstanceState != null) {
			mWebView.restoreState(savedInstanceState);
			loadUrl();
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			loadUrl();
			mWebView.loadUrl(AppConfig.CONTACT_US_URL);
			mProgressBar.setVisibility(View.VISIBLE);
		}
		if (!AppUtils.isInternetConnected(mContext)) {
			mProgressBar.setVisibility(View.GONE);
			showCustomDialog(getString(R.string.error),
					getString(R.string.no_internet), getString(R.string.yes),
					getString(R.string.no), true);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mWebView.saveState(outState);
	}

	/**
	 * Method to update web view settings.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void updateWebViewSettings() {
		boolean isCacheEnabled = SettingsPreferences.isCacheEnabled(mContext);
		boolean isJavascriptEnabled = SettingsPreferences
				.isJavascriptEnabled(mContext);
		boolean isScrollbarEnabled = SettingsPreferences
				.isScrollbarEnabled(mContext);
		boolean isZoomEnabled = SettingsPreferences.isZoomEnabled(mContext);

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setAppCacheEnabled(isCacheEnabled);
		if (isCacheEnabled) {
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		} else {
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		}

		webSettings.setJavaScriptEnabled(isJavascriptEnabled);

		mWebView.setHorizontalScrollBarEnabled(isScrollbarEnabled);

		webSettings.setBuiltInZoomControls(isZoomEnabled);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webSettings.setDisplayZoomControls(isZoomEnabled);
		} else {
			Class<?> classType;
			Field field;
			try {
				classType = WebView.class;
				field = classType.getDeclaredField("mZoomButtonsController");
				field.setAccessible(true);
				ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
						mWebView);
				if (isZoomEnabled) {
					mZoomButtonsController.getZoomControls().setVisibility(
							View.VISIBLE);
				} else {
					mZoomButtonsController.getZoomControls().setVisibility(
							View.GONE);
				}
				try {
					field.set(mWebView, mZoomButtonsController);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method to load web url in web view
	 */
	public void loadUrl() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setGeolocationEnabled(true);
		// webSettings.setPluginsEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setSupportZoom(true);
		// New code added ver 1.1
		mWebView.setFocusable(true);
		mWebView.setFocusableInTouchMode(true);
		mWebView.requestFocus();
		webSettings.setLightTouchEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDatabasePath("/data/data/" + getPackageName()
				+ "/databases/");
		webSettings.setGeolocationEnabled(true);
		webSettings.setGeolocationDatabasePath("/data/data/" + getPackageName()
				+ "/databases/");
		webSettings.setUseWideViewPort(true);
		webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

		mWebView.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);

		updateWebViewSettings();

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				mProgressBar.setProgress(progress);
				if (progress == 100) {
					mProgressBar.setVisibility(View.GONE);
				}
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				mProgressBar.setVisibility(View.GONE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
			}
		});
		mWebView.setWebChromeClient(new GeoWebChromeClient());
	}

	/**
	 * WebChromeClient subclass handles UI-related calls Note: think chrome as
	 * in decoration, not the Chrome browser
	 */
	public class GeoWebChromeClient extends WebChromeClient {
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			// Always grant permission since the app itself requires location
			// permission and the user has therefore already granted it
			callback.invoke(origin, true, false);
		}
	}

	/**
	 * Method to inits the ads banner view
	 */
	private void initAds() {
		if (mAdsView == null) {
			mAdsView = (RelativeLayout) findViewById(R.id.bottom_ads_view);
		}
		if (AppConfig.ADS_ENABLED) {
			AppUtils.addAdsBannerView(ContactUsActivity.this, mAdsView);
			mAdsView.setVisibility(View.VISIBLE);
		} else {
			mAdsView.setVisibility(View.GONE);
		}
	}

	/**
	 * Method to set init component.
	 */
	private void initViews() {
		mActionBroadcastReceiver = new ActionBrodcastListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstants.SETTINGS_UPDATED);
		intentFilter.addAction(AppConstants.UPDATE_BACKGROUND_THEME);

		registerReceiver(mActionBroadcastReceiver, intentFilter);
		mWebView = (WebView) findViewById(R.id.webview);
		mProgressBar = (CircleProgressBar) findViewById(R.id.progress_bar);
		mProgressBar.setColorSchemeColors(ThemeContentManager.getInstance()
				.getTheme(mContext));
		loadUrl();
		initAds();
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
		mHeaderView.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));
		mProgressBar.setColorSchemeColors(ThemeContentManager.getInstance()
				.getTheme(mContext));
	}

	/**
	 * Method to show custom dialog.
	 * 
	 * @param title
	 * @param message
	 * @param okText
	 * @param cancelText
	 * @param singleButtonEnabled
	 */
	private void showCustomDialog(String title, String message, String okText,
			String cancelText, final boolean singleButtonEnabled) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.custom_dialog_view, null);
		RelativeLayout titlebarView = (RelativeLayout) dialogView
				.findViewById(R.id.title_bar_view);
		View button_layout = dialogView.findViewById(R.id.button_layout);
		Button okButton = (Button) dialogView.findViewById(R.id.ok_button);
		Button cancelButton = (Button) dialogView
				.findViewById(R.id.cancel_button);
		TextView titleTextview = (TextView) dialogView
				.findViewById(R.id.title_textview);
		TextView messageTextview = (TextView) dialogView
				.findViewById(R.id.message_textview);
		button_layout.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));
		titlebarView.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}

		if (singleButtonEnabled) {
			ImageView button_divider = (ImageView) dialogView
					.findViewById(R.id.button_divider);
			button_divider.setVisibility(View.GONE);
			cancelButton.setVisibility(View.GONE);
			okButton.setText(getString(R.string.ok));
		} else {
			cancelButton.setVisibility(View.VISIBLE);
		}
		mCustomDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar);
		mCustomDialog.setContentView(dialogView);
		mCustomDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					dialog.dismiss();
				}
				return false;
			}
		});

		mCustomDialog.setCanceledOnTouchOutside(true);

		titleTextview.setText(title);
		messageTextview.setText(message);

		/**
		 * Listener for OK button click.
		 */
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mCustomDialog.dismiss();
				if (!singleButtonEnabled) {
					finish();
				}
			}
		});

		/**
		 * Listener for Cancel button click.
		 */
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mCustomDialog.dismiss();
			}
		});

		mCustomDialog.show();
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
			if (action.equalsIgnoreCase(AppConstants.SETTINGS_UPDATED)) {
				updateWebViewSettings();
			} else if (action.equals(AppConstants.UPDATE_BACKGROUND_THEME)) {
				setDefaultTheme(SettingsPreferences.getThemeIndex(mContext));
			}

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
		}
	}

	@Override
	protected void onDestroy() {
		if (mContext != null) {
			if (mActionBroadcastReceiver != null) {
				mContext.unregisterReceiver(mActionBroadcastReceiver);
				mActionBroadcastReceiver = null;
			}
			if (mCustomDialog != null) {
				mCustomDialog.dismiss();
				mCustomDialog = null;
			}
			mAdsView = null;
			mHeaderView = null;
			mContext = null;
			super.onDestroy();
		}
	}
}
