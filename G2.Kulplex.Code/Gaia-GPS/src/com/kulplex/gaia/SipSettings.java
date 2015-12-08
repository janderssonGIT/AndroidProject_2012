/**
 *	SipSettings
 *
 *	This class is based on the SipDemo, provided by Google. It has been modified for our project.
 *
 * 
 */

package com.kulplex.gaia;

import com.kulplex.gaia.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SipSettings extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // They're all in the XML file res/xml/preferences.xml.
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}