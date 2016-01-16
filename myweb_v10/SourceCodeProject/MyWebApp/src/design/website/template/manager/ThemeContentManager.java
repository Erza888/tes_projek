package design.website.template.manager;

import android.content.Context;
import design.website.template.R;
import design.website.template.model.ThemeItem;
import design.website.template.util.SettingsPreferences;

/**
 * This class is a manager class to manage and provide the contents.
 * 
 * @author vishalbodkhe
 * 
 */
public class ThemeContentManager {

	public static volatile ThemeContentManager contentManager;

	private ThemeItem themeItem;

	static {
		contentManager = new ThemeContentManager();
	}

	/**
	 * Method to get instance of ContentManager
	 * 
	 * @return contentManager
	 */
	public static ThemeContentManager getInstance() {
		if (contentManager == null) {
			contentManager = new ThemeContentManager();
		}
		return contentManager;
	}

	/**
	 * Hidden constructor
	 */
	private ThemeContentManager() {

	}

	public int getTheme(Context context) {
		int color = context.getResources().getColor(
				R.color.material_deep_teal_500);
		switch (SettingsPreferences.getThemeIndex(context)) {
		case 0:
			color = context.getResources().getColor(
					R.color.material_deep_teal_500);
			break;
		case 1:
			color = context.getResources().getColor(R.color.red);

			break;
		case 2:
			color = context.getResources().getColor(R.color.blue);

			break;
		case 3:
			color = context.getResources().getColor(
					R.color.material_blue_grey_800);
			break;

		}
		return color;
	}
}