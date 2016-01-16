/**
 * 
 */
package design.website.template.model;

import java.io.Serializable;

/**
 * The model class to hold the Theme color list item.
 * 
 * @author vishalbodkhe
 * 
 */
public class ThemeItem implements Serializable {
	/** The fields to hold different required values. */
	private String splashBackground;
	private String splashTextColor;
	private String headerFooterBackground;
	private String headerTextColor;
	private String popupBackground;
	private String screenBackground;

	/**
	 * Method to get splashBackground
	 * 
	 * @return the splashBackground
	 */
	public String getSplashBackground() {
		return splashBackground;
	}

	/**
	 * Method to set splashBackground
	 * 
	 * @param splashBackground
	 *            the splashBackground to set
	 */
	public void setSplashBackground(String splashBackground) {
		this.splashBackground = splashBackground;
	}

	/**
	 * Method to get splashTextColor
	 * 
	 * @return the splashTextColor
	 */
	public String getSplashTextColor() {
		return splashTextColor;
	}

	/**
	 * Method to set splashTextColor
	 * 
	 * @param splashTextColor
	 *            the splashTextColor to set
	 */
	public void setSplashTextColor(String splashTextColor) {
		this.splashTextColor = splashTextColor;
	}

	/**
	 * Method to get headerFooterBackground
	 * 
	 * @return the headerFooterBackground
	 */
	public String getHeaderFooterBackground() {
		return headerFooterBackground;
	}

	/**
	 * Method to set headerFooterBackground
	 * 
	 * @param headerFooterBackground
	 *            the headerFooterBackground to set
	 */
	public void setHeaderFooterBackground(String headerFooterBackground) {
		this.headerFooterBackground = headerFooterBackground;
	}

	/**
	 * Method to get headerTextColor
	 * 
	 * @return the headerTextColor
	 */
	public String getHeaderTextColor() {
		return headerTextColor;
	}

	/**
	 * Method to set headerTextColor
	 * 
	 * @param headerTextColor
	 *            the headerTextColor to set
	 */
	public void setHeaderTextColor(String headerTextColor) {
		this.headerTextColor = headerTextColor;
	}

	/**
	 * Method to get popupBackground
	 * 
	 * @return the popupBackground
	 */
	public String getPopupBackground() {
		return popupBackground;
	}

	/**
	 * Method to set popupBackground
	 * 
	 * @param popupBackground
	 *            the popupBackground to set
	 */
	public void setPopupBackground(String popupBackground) {
		this.popupBackground = popupBackground;
	}

	/**
	 * Method to get screenBackground
	 * 
	 * @return the screenBackground
	 */
	public String getScreenBackground() {
		return screenBackground;
	}

	/**
	 * Method to set screenBackground
	 * 
	 * @param screenBackground
	 *            the screenBackground to set
	 */
	public void setScreenBackground(String screenBackground) {
		this.screenBackground = screenBackground;
	}

}
