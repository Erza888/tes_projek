package design.website.template.model;

/**
 * The class to hold Navigation Drawer item values
 * 
 * @author vishalbodkhe
 */
public class NavDrawerItem {

	/** The fields to hold different required values. */
	private String title;
	private int icon;

	public NavDrawerItem() {
	}

	public NavDrawerItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	/**
	 * Method to get title
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Method to get icon
	 * 
	 * @return the icon
	 */
	public int getIcon() {
		return this.icon;
	}

	/**
	 * Method to set title
	 * 
	 * @return the title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Method to set icon
	 * 
	 * @return the icon
	 */
	public void setIcon(int icon) {
		this.icon = icon;
	}

}
