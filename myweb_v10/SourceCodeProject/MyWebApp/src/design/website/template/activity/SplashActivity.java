package design.website.template.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;
import design.website.template.BaseActivity;
import design.website.template.R;
import design.website.template.config.AppConfig;
import design.website.template.manager.ThemeContentManager;
import design.website.template.model.ThemeItem;
import design.website.template.util.SettingsPreferences;

/**
 * The class is to show splash view
 * 
 * @author vishalbodkhe
 * 
 */
public class SplashActivity extends BaseActivity {

	private Context mContext;
	private TextView mSplashTitle;
	private RelativeLayout mRootLayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		mContext = SplashActivity.this;
		mSplashTitle = (TextView) findViewById(R.id.splash_title);
		updateScreenBackground();
		mHandler.sendEmptyMessageDelayed(1, AppConfig.SPLASH_DURATION);
	}

	/**
	 * Handler to post message for splash duration and launch the home screen
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mHandler.removeMessages(1);
			Intent intent = new Intent(mContext, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	};

	/**
	 * Method to set toolbar theme
	 * 
	 * @param position
	 */
	private void updateScreenBackground() {
		if (mRootLayout == null) {
			mRootLayout = (RelativeLayout) findViewById(R.id.rootview);
		}
		mRootLayout.setBackgroundColor(ThemeContentManager.getInstance()
				.getTheme(mContext));
	}

	@Override
	protected void onDestroy() {
		if (mContext != null) {
			if (mHandler != null) {
				mHandler.removeMessages(1);
				mHandler = null;
			}
			mSplashTitle = null;
			mRootLayout = null;
			mContext = null;
			super.onDestroy();
		}
	}
}