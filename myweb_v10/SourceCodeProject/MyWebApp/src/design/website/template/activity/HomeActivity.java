/**
 * 
 */
package design.website.template.activity;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ZoomButtonsController;
import design.website.template.AndroidWebsiteApplication;
import design.website.template.R;
import design.website.template.adapter.MenuAdapter;
import design.website.template.config.AppConfig;
import design.website.template.lib.CircleProgressBar;
import design.website.template.lib.SmartDrawer;
import design.website.template.manager.ThemeContentManager;
import design.website.template.model.NavDrawerItem;
import design.website.template.util.AppConstants;
import design.website.template.util.AppUtils;
import design.website.template.util.SettingsPreferences;

/**
 * The home screen to show main website and action bar with reload and slider
 * icon to access slider options.
 * 
 * @author vishalbodkhe
 * 
 */
public class HomeActivity extends ActionBarActivity {
	private Context mContext;
	private WebView mWebView;
	private ImageView mArrowLeftImageview;
	private ImageView mArrowRightImageview;
	private RelativeLayout mBottomViewsLayout;
	private LinearLayout mFooterView;
	private DrawerLayout mDrawerLayout;
	private Dialog mCustomDialog;
	private CircleProgressBar mProgressBar;
	private ActionBarDrawerToggle mDrawerToggle;
	private ActionBrodcastListener mActionBroadcastReceiver;

	private ListView mDrawerList;
	private String[] mNavMenuTitles;

	private TypedArray mNavMenuIcons;
	private ArrayList<NavDrawerItem> mNavDrawerItems;
	private MenuAdapter mMenuAdapter;
	private RelativeLayout mAdsView;
	private Toolbar mToolbar;
	private ValueCallback<Uri> uploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!AndroidWebsiteApplication.isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		setContentView(R.layout.drawer_home_layout);
		mContext = HomeActivity.this;
		initViews();
		showToolbar();
		setDefaultTheme(SettingsPreferences.getThemeIndex(mContext));
		if (savedInstanceState != null) {
			mWebView.restoreState(savedInstanceState);
			loadUrl();
			mProgressBar.setVisibility(View.GONE);
		} else {
			loadUrl();
			mWebView.loadUrl(AppConfig.WEB_SITE_URL);
			mProgressBar.setVisibility(View.VISIBLE);
			updateArrows();
		}
		if (!AppUtils.isInternetConnected(mContext)) {
			mProgressBar.setVisibility(View.GONE);
			showCustomDialog(getString(R.string.error),
					getString(R.string.no_internet), getString(R.string.yes),
					getString(R.string.no), true);
		}
		updateArrows();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (uploadMessage == null)
				return;
			Uri result = intent == null || resultCode != RESULT_OK ? null
					: intent.getData();
			uploadMessage.onReceiveValue(result);
			uploadMessage = null;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mWebView.saveState(outState);
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
		mWebView.setVerticalScrollBarEnabled(isScrollbarEnabled);

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
	@SuppressWarnings("deprecation")
	public void loadUrl() {
		mWebView.setFocusable(true);
		mWebView.setFocusableInTouchMode(true);
		mWebView.requestFocus();
		WebSettings webSettings = mWebView.getSettings();
		// webSettings.setPluginsEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setSupportZoom(true);
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
			// For Android 3.0+
			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				uploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				HomeActivity.this.startActivityForResult(
						Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);
			}

			// For Android 3.0+
			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType) {
				uploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				HomeActivity.this.startActivityForResult(
						Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);
			}

			// For Android 4.1
			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				uploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				HomeActivity.this.startActivityForResult(
						Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);
			}

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
				mWebView.setWebChromeClient(new WebChromeClient() {
					// For Android 3.0+
					@SuppressWarnings("unused")
					public void openFileChooser(ValueCallback<Uri> uploadMsg) {
						uploadMessage = uploadMsg;
						Intent i = new Intent(Intent.ACTION_GET_CONTENT);
						i.addCategory(Intent.CATEGORY_OPENABLE);
						i.setType("*/*");
						HomeActivity.this.startActivityForResult(
								Intent.createChooser(i, "File Chooser"),
								FILECHOOSER_RESULTCODE);
					}

					// For Android 3.0+
					@SuppressWarnings("unused")
					public void openFileChooser(ValueCallback<Uri> uploadMsg,
							String acceptType) {
						uploadMessage = uploadMsg;
						Intent i = new Intent(Intent.ACTION_GET_CONTENT);
						i.addCategory(Intent.CATEGORY_OPENABLE);
						i.setType("*/*");
						HomeActivity.this.startActivityForResult(
								Intent.createChooser(i, "File Chooser"),
								FILECHOOSER_RESULTCODE);
					}

					// For Android 4.1
					@SuppressWarnings("unused")
					public void openFileChooser(ValueCallback<Uri> uploadMsg,
							String acceptType, String capture) {
						uploadMessage = uploadMsg;
						Intent i = new Intent(Intent.ACTION_GET_CONTENT);
						i.addCategory(Intent.CATEGORY_OPENABLE);
						i.setType("*/*");
						HomeActivity.this.startActivityForResult(
								Intent.createChooser(i, "File Chooser"),
								FILECHOOSER_RESULTCODE);
					}

					@Override
					public void onProgressChanged(WebView view, int progress) {
						if (mProgressBar != null) {
							mProgressBar.setProgress(progress);
							if (progress == 100) {
								mProgressBar.setVisibility(View.GONE);
							}
						}
					}
				});
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				updateArrows();
				mProgressBar.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (AppUtils.isInternetConnected(mContext)) {
					try {
						Intent intent = null;
						if (url.startsWith("tel:")) {
							intent = new Intent(Intent.ACTION_DIAL, Uri
									.parse(url));
							HomeActivity.this.startActivity(intent);
							return true;
						} else if ((url.endsWith(".pdf"))
								|| (url.endsWith(".txt"))
								|| (url.endsWith(".zip"))
								|| (url.endsWith(".rar"))
								|| (url.endsWith(".xls"))
								|| (url.endsWith(".xlsx"))
								|| (url.endsWith(".doc"))
								|| (url.endsWith(".docx"))
								|| (url.endsWith(".pages"))
								|| (url.endsWith(".ai"))
								|| (url.endsWith(".ppt"))
								|| (url.endsWith(".pptx"))
								|| (url.endsWith(".dxf"))
								|| (url.endsWith(".svg"))
								|| (url.endsWith(".psd"))
								|| (url.endsWith(".tiff"))
								|| (url.endsWith(".ttf"))
								|| (url.endsWith(".xps"))
								|| (url.endsWith(".eps"))
								|| (url.endsWith(".ps"))) {
							HomeActivity.this.mWebView
									.loadUrl("https://docs.google.com/viewer?url="
											+ url);
							return true;
						} else if (url.startsWith("sms:")) {
							intent = new Intent(Intent.ACTION_SENDTO, Uri
									.parse(url));
							HomeActivity.this.startActivity(intent);
							return true;
						} else if (url.startsWith("share:")) {
							String[] arrayOfString = url.split(":");
							try {
								String str = URLDecoder.decode(
										arrayOfString[1], "UTF-8");
								intent = new Intent(Intent.ACTION_SEND);
								intent.setType("text/plain");
								intent.putExtra(Intent.EXTRA_TEXT,
										"http://www.youtube.com/watch?v=" + str);
								intent.putExtra(Intent.EXTRA_SUBJECT,
										"Interesting for you, check this!");
								HomeActivity.this.startActivity(Intent
										.createChooser(intent,
												getString(R.string.share_title)));
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							return true;
						} else if ((url.endsWith(".jpg"))
								|| (url.endsWith(".png")
										|| (url.endsWith(".PNG"))
										|| (url.endsWith(".JPG"))
										|| (url.endsWith(".gif"))
										|| (url.endsWith(".GIF"))
										|| (url.endsWith(".jpeg")) || (url
											.endsWith(".JPEG")))) {
							intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.parse(url), "image/*");
							HomeActivity.this.startActivity(intent);
							return true;
						} else if ((url.endsWith(".mp3"))
								|| (url.endsWith(".MP3"))) {
							intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.parse(url), "audio/mp3");
							HomeActivity.this.startActivity(intent);
							return true;
						} else if ((url.endsWith(".mp4"))
								|| (url.endsWith(".MP4"))) {
							intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.parse(url), "video/mp4");
							HomeActivity.this.startActivity(intent);
							return true;
						} else if (url.endsWith(".3gp")) {
							intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.parse(url), "video/3gp");
							HomeActivity.this.startActivity(intent);
							return true;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					mProgressBar.setVisibility(View.GONE);
					showCustomDialog(getString(R.string.error),
							getString(R.string.no_internet),
							getString(R.string.yes), getString(R.string.no),
							true);
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

		});
		mWebView.setWebChromeClient(new GeoWebChromeClient());
	}

	/**
	 * Method to inits the ads banner view
	 */
	private void initAds() {
		if (mAdsView == null) {
			mAdsView = (RelativeLayout) findViewById(R.id.bottom_ads_view);
		}
		if (AppConfig.ADS_ENABLED) {
			mAdsView.setVisibility(View.VISIBLE);
			AppUtils.addAdsBannerView(HomeActivity.this, mAdsView);
		} else {
			mAdsView.setVisibility(View.GONE);
		}
	}

	/**
	 * Method to set toolbar theme
	 * 
	 * @param position
	 */
	private void setDefaultTheme(int position) {
		if (mFooterView == null) {
			mFooterView = (LinearLayout) findViewById(R.id.bottomLinear_view);
		}
		mDrawerList.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));
		mToolbar.setBackgroundColor(ThemeContentManager.getInstance().getTheme(
				mContext));
		mFooterView.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));
		mProgressBar.setColorSchemeColors(ThemeContentManager.getInstance()
				.getTheme(mContext));
	}

	/**
	 * Method to show toolbar and drawer layout
	 */
	private void showToolbar() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
		}
		mToolbar.setTitleTextAppearance(mContext, R.style.TitleTextStyle);
		mDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this,
				mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mNavMenuTitles = getResources()
				.getStringArray(R.array.nav_drawer_items);
		mNavMenuIcons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);
		mNavDrawerItems = new ArrayList<NavDrawerItem>();
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0], mNavMenuIcons
				.getResourceId(0, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1], mNavMenuIcons
				.getResourceId(1, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2], mNavMenuIcons
				.getResourceId(2, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3], mNavMenuIcons
				.getResourceId(3, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[4], mNavMenuIcons
				.getResourceId(4, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[5], mNavMenuIcons
				.getResourceId(5, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[6], mNavMenuIcons
				.getResourceId(6, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[7], mNavMenuIcons
				.getResourceId(7, -1)));
		mNavMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		mMenuAdapter = new MenuAdapter(HomeActivity.this);
		mMenuAdapter.setList(mNavDrawerItems);
		mDrawerList.setAdapter(mMenuAdapter);
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			onItemClickListerner(position, view);
		}
	}

	/**
	 * Display selected navigation drawer list item
	 * */
	private void onItemClickListerner(int position, View view) {
		Intent intent = null;
		switch (position) {
		case AppConstants.MENU_1:
			intent = new Intent(mContext, SettingsActivity.class);
			startActivity(intent);
			mDrawerLayout.closeDrawers();
			break;
		case AppConstants.MENU_2:
			doCall();
			mDrawerLayout.closeDrawers();
			break;
		case AppConstants.MENU_3:
			sendEmail();
			mDrawerLayout.closeDrawers();
			break;
		case AppConstants.MENU_4:
			shareClicked(getString(R.string.share_subject),
					AndroidWebsiteApplication.getAppUrl());
			mDrawerLayout.closeDrawers();
			break;
		case AppConstants.MENU_5:
			intent = new Intent(mContext, ContactUsActivity.class);
			startActivity(intent);
			mDrawerLayout.closeDrawers();
			break;
		case AppConstants.MENU_6:
			SmartDrawer smartDrawer = (SmartDrawer) view
					.findViewById(R.id.drawer);
			smartDrawer.animateToggle();
			break;
		case AppConstants.MENU_7:
			intent = new Intent(mContext, AboutActivity.class);
			startActivity(intent);
			mDrawerLayout.closeDrawers();
			break;
		case AppConstants.MENU_8:
			showCustomDialog(getString(R.string.confirmation),
					getString(R.string.exit_msg), getString(R.string.yes),
					getString(R.string.no), false);
			mDrawerLayout.closeDrawers();
			break;
		}

	}

	/**
	 * Method to process on left arrow click.
	 */
	private void processLeftArrowClick() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			onBackPressed();
		}
		updateArrows();
	}

	/**
	 * Method to process on right arrow click.
	 */
	private void processRightArrowClick() {
		if (mWebView.canGoForward()) {
			mWebView.goForward();
		}
		updateArrows();
	}

	/**
	 * Method to update arrows
	 */
	private void updateArrows() {
		if (mWebView.canGoBack()) {
			mArrowLeftImageview
					.setImageResource(R.drawable.ic_action_arrow_left);
		} else {
			mArrowLeftImageview
					.setImageResource(R.drawable.ic_action_arrow_left_disabled);
		}
		if (mWebView.canGoForward()) {
			mArrowRightImageview
					.setImageResource(R.drawable.ic_action_arrow_right);
		} else {
			mArrowRightImageview
					.setImageResource(R.drawable.ic_action_arrow_right_disabled);
		}
	}

	/**
	 * Method to get click actions on registered views.
	 * 
	 * @param view
	 */
	public void viewClickHandler(View view) {
		switch (view.getId()) {
		case R.id.arrow_left_view:
			processLeftArrowClick();
			break;
		case R.id.arrow_right_view:
			processRightArrowClick();
			break;
		case R.id.reload_view:
			if (!AppUtils.isInternetConnected(mContext)) {
				mProgressBar.setVisibility(View.GONE);
				showCustomDialog(getString(R.string.error),
						getString(R.string.no_internet),
						getString(R.string.yes), getString(R.string.no), true);
			} else {
				mWebView.reload();
			}
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		} else if (item.getItemId() == R.id.menu_home) {
			loadUrl();
			mWebView.loadUrl(AppConfig.WEB_SITE_URL);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Method to send email.
	 */
	private void sendEmail() {
		try {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("message/rfc822");
			emailIntent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { AppConfig.CONTACT_EMAIL });
			startActivity(Intent.createChooser(emailIntent,
					getString(R.string.send_email_title)));
		} catch (ActivityNotFoundException ex) {
			AppUtils.showToast(mContext, getString(R.string.no_email_supported));
		}
	}

	/**
	 * Method to share app on social network
	 */
	private void shareClicked(String subject, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(Intent.createChooser(intent,
				getString(R.string.share_title)));
	}

	/**
	 * Method to make call.
	 */
	private void doCall() {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"
					+ AppConfig.CONTACT_PHONE_NUMBER));
			startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			AppUtils.showToast(mContext, getString(R.string.no_phone_supported));
		}
	}

	/**
	 * Method to show inits component
	 */
	private void initViews() {
		mBottomViewsLayout = (RelativeLayout) findViewById(R.id.bottom_views_layout);
		mArrowLeftImageview = (ImageView) findViewById(R.id.arrow_left_imageview);
		mArrowRightImageview = (ImageView) findViewById(R.id.arrow_right_imageview);

		mActionBroadcastReceiver = new ActionBrodcastListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstants.SETTINGS_UPDATED);
		intentFilter.addAction(AppConstants.UPDATE_BACKGROUND_THEME);
		registerReceiver(mActionBroadcastReceiver, intentFilter);
		
		
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setBackgroundColor(Color.TRANSPARENT);
		mProgressBar = (CircleProgressBar) findViewById(R.id.progress_bar);
		mProgressBar.setColorSchemeColors(ThemeContentManager.getInstance()
				.getTheme(mContext));
		
		
		if (AppConfig.SLIDING_MENU_ENABLED) {
			if (mToolbar != null) {
				mToolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
				mDrawerLayout.setDrawerLockMode(0);
			}
		} else {
			if (mToolbar != null) {
				mToolbar.setNavigationIcon(null);
				mDrawerLayout.setDrawerLockMode(1);
			}
		}
		if (AppConfig.BOTTOM_BAR_ENABLED) {
			mBottomViewsLayout.setVisibility(View.VISIBLE);
		} else {
			mBottomViewsLayout.setVisibility(View.GONE);
		}

		if (AppConfig.TOP_BAR_ENABLED) {
			if (mToolbar != null) {
				mToolbar.setVisibility(View.VISIBLE);
				mDrawerLayout.setDrawerLockMode(0);
			}
		} else {
			if (mToolbar != null) {
				mToolbar.setVisibility(View.GONE);
				mDrawerLayout.setDrawerLockMode(1);
			}
		}

		initAds();
	}

	/**
	 * The class is used to get broadcasted action for which it is registered
	 * 
	 * @author vishalbodkhe
	 * 
	 */
	public class ActionBrodcastListener extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(AppConstants.SETTINGS_UPDATED)) {
				updateWebViewSettings();
				if (!AppUtils.isInternetConnected(mContext)) {
					mProgressBar.setVisibility(View.GONE);
					showCustomDialog(getString(R.string.error),
							getString(R.string.no_internet),
							getString(R.string.yes), getString(R.string.no),
							true);
				} else {
					mWebView.reload();
				}
			} else if (action.equals(AppConstants.UPDATE_BACKGROUND_THEME)) {
				setDefaultTheme(SettingsPreferences.getThemeIndex(mContext));
			}
		}
	}

	@Override
	public void onBackPressed() {
		showCustomDialog(getString(R.string.confirmation),
				getString(R.string.exit_msg), getString(R.string.yes),
				getString(R.string.no), false);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (AppConfig.SLIDING_MENU_ENABLED) {
			mDrawerToggle.syncState();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (AppConfig.SLIDING_MENU_ENABLED) {
			mDrawerToggle.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
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
			if (mMenuAdapter != null) {
				mMenuAdapter.cleanUp();
				mMenuAdapter = null;
			}
			mBottomViewsLayout = null;
			mAdsView = null;
			mArrowLeftImageview = null;
			mArrowRightImageview = null;
			mWebView = null;
			mFooterView = null;
			mDrawerToggle = null;
			mDrawerLayout = null;
			mProgressBar = null;
			mContext = null;
			super.onDestroy();
		}
	}
}