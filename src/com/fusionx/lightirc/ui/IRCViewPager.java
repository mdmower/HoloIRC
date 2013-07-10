package com.fusionx.lightirc.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.fusionx.lightirc.adapters.IRCPagerAdapter;
import com.fusionx.lightirc.fragments.ircfragments.ChannelFragment;
import com.fusionx.lightirc.fragments.ircfragments.PMFragment;
import com.fusionx.lightirc.fragments.ircfragments.ServerFragment;

public class IRCViewPager extends ViewPager {
    public IRCViewPager(final Context context) {
        super(context);
    }

    @Override
    public IRCPagerAdapter getAdapter() {
        return (IRCPagerAdapter) super.getAdapter();
    }

    public int onNewChannelJoined(final String serverName, final String channelName, final boolean mentioned) {
        final ChannelFragment channel = new ChannelFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("title", channelName);
        bundle.putString("serverName", serverName);

        channel.setArguments(bundle);

        final int position = getAdapter().addFragment(channel);

        if (mentioned) {
            setCurrentItem(position, true);
        }

        return position;
    }

    public int onNewPrivateMessage(final String serverName, final String userNick) {
        final PMFragment pmFragment = new PMFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("serverName", serverName);
        bundle.putString("title", userNick);
        pmFragment.setArguments(bundle);

        final int position = getAdapter().addFragment(pmFragment);

        setCurrentItem(position, true);

        return position;
    }

    public void addServerFragment(final String serverName) {
        final ServerFragment fragment = new ServerFragment();
        final Bundle bundle = new Bundle();
        bundle.putString("title", serverName);
        fragment.setArguments(bundle);

        getAdapter().addFragment(fragment);
    }

    public void disconnect() {
        getAdapter().removeAllButServer();
        getAdapter().disableAllEditTexts();
    }
}
