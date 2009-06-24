package net.necomimi.android.utut;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

public class SettingActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {
	private static final String EMPTY = "";
	public static final String CONFIG_TWIT_ENABLED_KEY = "config_twit_enabled";
	public static final String CONFIG_TWIT_ID_KEY = "config_twit_id";
	public static final String CONFIG_TWIT_PASSWORD_KEY = "config_twit_password";
	public static final String CONFIG_SOUND_KEY = "config_sound_file";
	public static final String CONFIG_SOUND_DEFAULT = "castanet";
	private static final String MASK_TEXT = "*****";
	private Preference prefTwitId;
	private Preference prefTwitPassword;
	private Preference prefSound;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.config);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefTwitId = findPreference(CONFIG_TWIT_ID_KEY);
		this.prefTwitId.setOnPreferenceChangeListener(this);
		this.prefTwitId.setSummary(pref.getString(CONFIG_TWIT_ID_KEY, EMPTY));
		this.prefTwitPassword = findPreference(CONFIG_TWIT_PASSWORD_KEY);
		this.prefTwitPassword.setOnPreferenceChangeListener(this);
		this.prefSound = findPreference(CONFIG_SOUND_KEY);
		this.prefSound.setOnPreferenceChangeListener(this);
		this.prefSound.setSummary(pref.getString(CONFIG_SOUND_KEY, EMPTY));
		if (!EMPTY.equals(pref.getString(CONFIG_TWIT_PASSWORD_KEY, EMPTY))) {
			this.prefTwitPassword.setSummary(MASK_TEXT);
		}
    }	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
		case KeyEvent.KEYCODE_BACK:
			setResult(RESULT_OK);
			finish();
			
		default:
		}		
		return super.onKeyDown(keyCode, event);
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (CONFIG_TWIT_ID_KEY.equals(preference.getKey())) {
			this.prefTwitId.setSummary((String)newValue);
		} else if (CONFIG_TWIT_PASSWORD_KEY.equals(preference.getKey())) {
			this.prefTwitPassword.setSummary(MASK_TEXT);			
		} else if (CONFIG_SOUND_KEY.equals(preference.getKey())) {
			this.prefSound.setSummary((String)newValue);
		}
		return true;
	}
	
}
