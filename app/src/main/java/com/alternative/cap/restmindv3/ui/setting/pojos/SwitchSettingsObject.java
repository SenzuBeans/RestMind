package com.alternative.cap.restmindv3.ui.setting.pojos;

import android.content.SharedPreferences;

import com.alternative.cap.restmindv3.R;

import java.io.Serializable;

/**
 * a setting object which contains a {@link android.widget.Switch}
 */
@SuppressWarnings("PointlessBooleanExpression")
public class SwitchSettingsObject extends BooleanSettingsObject implements Serializable
{
	public SwitchSettingsObject(Builder builder)
	{
		super(builder);
	}

	@Override
	public int getLayout()
	{
		return R.layout.settings_switch_object;
	}

	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////

	public static class Builder extends BooleanSettingsObject.Builder
	{
		/**
		 *
		 * @param key the key for this {@link SwitchSettingsObject}
		 *            to be saved in the apps' {@link SharedPreferences}
		 * @param title the title for this {@link SwitchSettingsObject}
		 * @param defaultValue
		 */
        public Builder(String key,
                       String title,
                       boolean defaultValue)
        {
            super(key,
                  title,
                  defaultValue,
                  R.id.textView_switchSettingsObject_title,
                  R.id.textView_switchSettingsObject_summary,
				  R.id.switch_switchSettingsObject,
				  R.id.imageView_switchSettingsObject_icon);
        }

        @Override
		public SwitchSettingsObject build()
		{
			return new SwitchSettingsObject(this);
		}
	}
}
