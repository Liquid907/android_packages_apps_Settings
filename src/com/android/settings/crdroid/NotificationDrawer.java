/*
 * Copyright (C) 2013 SlimRoms
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.crdroid;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;

import com.android.internal.util.crdroid.DeviceUtils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class NotificationDrawer extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String TAG = "NotificationDrawerSettings";
    private static final String PREF_NOTIFICATION_OPTIONS = "options";
    private static final String PREF_NOTIFICATION_HIDE_CARRIER = "notification_hide_carrier";

    CheckBoxPreference mHideCarrier;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.notification_drawer_settings);

 	PreferenceScreen prefs = getPreferenceScreen();

        mHideCarrier = (CheckBoxPreference) findPreference(PREF_NOTIFICATION_HIDE_CARRIER);
        boolean hideCarrier = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.NOTIFICATION_HIDE_CARRIER, 0) == 1;
        mHideCarrier.setChecked(hideCarrier);
        mHideCarrier.setOnPreferenceChangeListener(this);

        PreferenceCategory additionalOptions =
            (PreferenceCategory) prefs.findPreference(PREF_NOTIFICATION_OPTIONS);

        PackageManager pm = getPackageManager();
        boolean isMobileData = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

        if (!DeviceUtils.isPhone(getActivity())
            || !DeviceUtils.deviceSupportsMobileData(getActivity())) {
            // Nothing for tablets, large screen devices and non mobile devices which doesn't show
            // information in notification drawer.....remove options
            additionalOptions.removePreference(mHideCarrier);
            prefs.removePreference(additionalOptions);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
	if (preference == mHideCarrier) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NOTIFICATION_HIDE_CARRIER,
                    (Boolean) newValue ? 1 : 0);
            return true;
        }
        return false;
    }
}
