package design.website.template;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import design.website.template.util.AppConstants;

/**
 * The class is used to show custom TextView style
 * 
 * @author vishalbodkhe
 * 
 */
public class CustomDescriptionTextview extends TextView {
	private Context mContext;
	private String mTTFName;

	public CustomDescriptionTextview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mTTFName = AppConstants.FONT_NAME;
		setTypeface();
	}

	/**
	 * This method used to set the typeface.
	 */
	private void setTypeface() {
		Typeface font = Typeface.createFromAsset(mContext.getAssets(),
				AppConstants.FONTS + mTTFName);
		setTypeface(font);
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}
}
