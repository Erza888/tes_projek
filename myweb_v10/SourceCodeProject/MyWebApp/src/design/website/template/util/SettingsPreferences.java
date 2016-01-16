/**
 * 
 */
package design.website.template.util;

import android.content.Context;
import android.content.SharedPreferences;
import design.website.template.config.AppConfig;

/**
 * The class which provides the access to read/write settings in shared
 * preference.
 * 
 * @author vishalbodkhe
 * 
 */
public class SettingsPreferences {

	private static final String WEBSITE_PREFS = "website_app_prefs";
	private static final String CACHE_ENABLED = "chache_enabled";
	private static final String JAVASCRIPT_ENABLED = "javascript_enabled";
	private static final String SCROLLBAR_ENABLED = "scrollbar_enabled";
	private static final String ZOOM_ENABLED = "zoom_enabled";

	private static final String THEME_INDEX = "themeindex";

	/**
	 * Method to set login
	 * 
	 * @param context
	 * @param status
	 */
	public static void seThemeIndex(Context context, int position) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putInt(THEME_INDEX, position);
		prefEditor.commit();
	}

	/**
	 * Method to check is login
	 * 
	 * @param context
	 * @return
	 */
	public static int getThemeIndex(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_PRIVATE);
		return prefs.getInt(THEME_INDEX, AppConfig.DEFAULT_THEME_COLOR);
	}

	/**
	 * Method to set cache enabled status.
	 * 
	 * @param context
	 * @param result
	 */
	public static void setCacheEnabled(Context context, boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(CACHE_ENABLED, result);
		prefEditor.commit();
	}

	/**
	 * Method to get cache enabled status
	 * 
	 * @param context
	 * @return true or false
	 */
	public static boolean isCacheEnabled(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		return prefs.getBoolean(CACHE_ENABLED, AppConfig.DEFAULT_CACHE_ENABLED);
	}

	/**
	 * Method to set javascript enabled status.
	 * 
	 * @param context
	 * @param result
	 */
	public static void setJavascriptEnabled(Context context, boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(JAVASCRIPT_ENABLED, result);
		prefEditor.commit();
	}

	/**
	 * Method to get javascript enabled status
	 * 
	 * @param context
	 * @return true or false
	 */
	public static boolean isJavascriptEnabled(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		return prefs.getBoolean(JAVASCRIPT_ENABLED,
				AppConfig.DEFAULT_JAVASCRIPT_ENABLED);
	}

	/**
	 * Method to set scrollbar enabled status.
	 * 
	 * @param context
	 * @param result
	 */
	public static void setScrollbarEnabled(Context context, boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(SCROLLBAR_ENABLED, result);
		prefEditor.commit();
	}

	/**
	 * Method to get scrollbar enabled status
	 * 
	 * @param context
	 * @return true or false
	 */
	public static boolean isScrollbarEnabled(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		return prefs.getBoolean(SCROLLBAR_ENABLED,
				AppConfig.DEFAULT_SCROLLBARS_ENABLED);
	}

	/**
	 * Method to set zoom enabled status.
	 * 
	 * @param context
	 * @param result
	 */
	public static void setZoomEnabled(Context context, boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(ZOOM_ENABLED, result);
		prefEditor.commit();
	}

	/**
	 * Method to get zoom enabled status
	 * 
	 * @param context
	 * @return true or false
	 */
	public static boolean isZoomEnabled(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(WEBSITE_PREFS,
				Context.MODE_WORLD_WRITEABLE);
		return prefs.getBoolean(ZOOM_ENABLED,
				AppConfig.DEFAULT_ZOOM_CONTROL_ENABLED);
	}
}
