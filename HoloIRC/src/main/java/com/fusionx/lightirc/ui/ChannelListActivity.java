package com.fusionx.lightirc.ui;

import com.fusionx.lightirc.interfaces.IServerSettings;
import com.fusionx.lightirc.util.UIUtils;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBarActivity;

public class ChannelListActivity extends ActionBarActivity implements IServerSettings {

    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(UIUtils.getThemeInt(this));

        super.onCreate(savedInstanceState);

        mFileName = getIntent().getStringExtra("filename");

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                new ChannelListFragment()).commit();
    }

    @Override
    public String getFileName() {
        return mFileName;
    }

    @Override
    public void setupPreferences(PreferenceScreen screen, Activity activity) {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        throw new IllegalArgumentException();
    }
}