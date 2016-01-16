package design.website.template.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import design.website.template.R;
import design.website.template.config.AppConfig;
import design.website.template.lib.SmartDrawer;
import design.website.template.model.NavDrawerItem;
import design.website.template.util.AppConstants;
import design.website.template.util.SettingsPreferences;

/**
 * The adapter class to bind the views on list view with contents.
 * 
 * @author VishalMobitech
 * 
 */
public class MenuAdapter extends BaseAdapter {

	private Context mContext;
	private RadioButtonClickListener mClickListener;

	/** Holds Layout Inflater to inflate list item. */
	private LayoutInflater mLayoutInflator;

	/** Holds the list */
	private ArrayList<NavDrawerItem> mListItems;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public MenuAdapter(Context context) {
		super();
		mContext = context;
		mLayoutInflator = (LayoutInflater) context.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Method to set the list.
	 * 
	 * @param list
	 */
	public void setList(ArrayList<NavDrawerItem> list) {
		mListItems = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final NavDrawerItem item = getItem(position);
		if (view == null) {
			view = mLayoutInflator.inflate(R.layout.row_menu, null);
			ViewHolder holder = new ViewHolder();
			holder.tvTitle = (TextView) view.findViewById(R.id.text_name);
			holder.icon = (ImageView) view.findViewById(R.id.image);
			holder.red_radio = (ImageView) view
					.findViewById(R.id.red_radio_imageview);
			holder.blue_radio = (ImageView) view
					.findViewById(R.id.blue_radio_imageview);
			holder.gray_radio = (ImageView) view
					.findViewById(R.id.grey_radio_imageview);
			holder.default_radio = (ImageView) view
					.findViewById(R.id.default_radio_imageview);
			holder.smartDrawer = (SmartDrawer) view.findViewById(R.id.drawer);

			holder.theme_default = (LinearLayout) view
					.findViewById(R.id.theme_default);
			holder.theme_red = (LinearLayout) view.findViewById(R.id.theme_red);
			holder.theme_blue = (LinearLayout) view
					.findViewById(R.id.theme_blue);
			holder.theme_grey = (LinearLayout) view
					.findViewById(R.id.theme_grey);

			view.setTag(holder);
		}

		final ViewHolder viewHolder = (ViewHolder) view.getTag();
		if (viewHolder != null && item != null) {
			setTheme(viewHolder);
			viewHolder.tvTitle.setText(item.getTitle());
			viewHolder.icon.setImageResource(item.getIcon());
			viewHolder.theme_red.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext, AppConfig.RED);
					setTheme(viewHolder);
					dismissSmartDrawer(view);
				}
			});
			viewHolder.theme_blue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext, AppConfig.BLUE);
					setTheme(viewHolder);
					dismissSmartDrawer(view);
				}
			});
			viewHolder.theme_grey.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext, AppConfig.GREY);
					setTheme(viewHolder);
					dismissSmartDrawer(view);
				}
			});
			viewHolder.theme_default.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					SettingsPreferences.seThemeIndex(mContext,
							AppConfig.DEFAULT_COLOR);
					setTheme(viewHolder);
					dismissSmartDrawer(view);
				}
			});
		}
		return view;
	}

	/**
	 * Method to set theme
	 * 
	 * @param viewHolder
	 * @param view
	 */
	private void setTheme(ViewHolder viewHolder) {
		Intent intent = new Intent(AppConstants.UPDATE_BACKGROUND_THEME);
		mContext.sendBroadcast(intent);
		switch (SettingsPreferences.getThemeIndex(mContext)) {
		case AppConfig.DEFAULT_COLOR:
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;

		case AppConfig.RED:
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;
		case AppConfig.BLUE:
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;
		case AppConfig.GREY:
			viewHolder.gray_radio
					.setBackgroundResource(R.drawable.ic_radio_button_on);
			viewHolder.blue_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.red_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			viewHolder.default_radio
					.setBackgroundResource(R.drawable.ic_radio_button_off);
			break;
		}
	}

	/**
	 * Method to dismiss smart drawer
	 * 
	 * @param v
	 */
	public void dismissSmartDrawer(View v) {
		final LinearLayout linearLayout = (LinearLayout) v;
		linearLayout.postDelayed(new Runnable() {

			@Override
			public void run() {
				SmartDrawer smartDrawer = (SmartDrawer) linearLayout
						.getParent().getParent();
				smartDrawer.animateClose();
			}
		}, 200);
	}

	@Override
	public int getCount() {
		if (mListItems != null) {
			return mListItems.size();
		}
		return 0;
	}

	@Override
	public NavDrawerItem getItem(int postion) {
		if (mListItems != null) {
			return mListItems.get(postion);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ViewHolder class to hold the views to bind on listview.
	 * 
	 * @author VishalMobitech
	 * 
	 */
	static class ViewHolder {
		TextView tvTitle;
		ImageView icon;
		ImageView red_radio;
		ImageView blue_radio;
		ImageView gray_radio;
		ImageView default_radio;
		SmartDrawer smartDrawer;
		LinearLayout theme_default;
		LinearLayout theme_red;
		LinearLayout theme_blue;
		LinearLayout theme_grey;

	}

	/**
	 * Interface to send click actions on activity.
	 * 
	 * @author VishalMobitech
	 * 
	 */
	public interface RadioButtonClickListener {
		public void onRadioClick(int position, NavDrawerItem item);
	}

	/**
	 * Method to set radio click listener to send events
	 * 
	 * @param listener
	 */
	public void setOnRadioListener(RadioButtonClickListener listener) {
		mClickListener = listener;
	}

	/**
	 * Method to used for freeing up the resources
	 */
	public void cleanUp() {
		mListItems = null;
		mLayoutInflator = null;
		mContext = null;
	}

}
