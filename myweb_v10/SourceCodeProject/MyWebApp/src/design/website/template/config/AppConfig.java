package design.website.template.config;

import design.website.template.R;

/**
 * The class to hold the configurable parameters to change the colors and other
 * things.
 * 
 * @author vishalbodkhe
 * 
 */
public class AppConfig {

	// Splash screen duration.
	public static final int SPLASH_DURATION = 2000;

	// For testing app make it true, once you ready with all to publish make it
	// false.
	public static final boolean TESTING_ENABLED = false;

	// Below Url need to be change as per your web site urls.
	public static final String WEB_SITE_URL = "http://beershopee.com";
	public static final String CONTACT_US_URL = "http://www.google.co.in";
	public static final String ABOUT_URL = "http://www.google.co.in";

	// Below fields needs to be change for phone and contact email.
	public static final String CONTACT_PHONE_NUMBER = "1234567890";
	public static final String CONTACT_EMAIL = "testemail@gmail.com";

	// Default settings
	public static final boolean DEFAULT_CACHE_ENABLED = true;
	public static final boolean DEFAULT_JAVASCRIPT_ENABLED = true;
	public static final boolean DEFAULT_SCROLLBARS_ENABLED = true;
	public static final boolean DEFAULT_ZOOM_CONTROL_ENABLED = true;

	// Below fields to show Bottom bar enable
	public static final boolean BOTTOM_BAR_ENABLED = true;

	// Below fields to show sliding menu enable
	public static final boolean SLIDING_MENU_ENABLED = true;

	// Below fields to show top action bar enable
	public static final boolean TOP_BAR_ENABLED = true;

	// Default Admob Ads status
	public static final boolean ADS_ENABLED = true;
	public static final boolean INTERSTITIAL_ADS_ENABLED = true;
	public static final String ADMOB_ADS_ID = "YOUR_ADMOB_BANNER_ID";
	public static final String INTERSTITIAL_ADS_ID = "YOUR_ADMOB_BANNER/INTERSTITIAL_ID";

	// Default theme color variable
	public static final int DEFAULT_THEME_COLOR = 0;

	// Theme color
	public static final int DEFAULT_COLOR = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	public static final int GREY = 3;


}
