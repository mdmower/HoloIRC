/*
    LightIRC - an IRC client for Android

    Copyright 2013 Lalit Maganti

    This file is part of LightIRC.

    LightIRC is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    LightIRC is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with LightIRC. If not, see <http://www.gnu.org/licenses/>.
 */

package com.fusionx.lightirc.listeners;

import com.fusionx.lightirc.R;
import com.fusionx.lightirc.irc.IRCUserComparator;
import com.fusionx.lightirc.parser.EventParser;
import com.fusionx.lightirc.service.IRCService;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.events.lightirc.IOExceptionEvent;
import org.pircbotx.hooks.events.lightirc.NickChangeEventPerChannel;
import org.pircbotx.hooks.events.lightirc.QuitEventPerChannel;

import java.util.ArrayList;
import java.util.Collections;

public class ServiceListener extends GenericListener {
    private final IRCService mService;

    public ServiceListener(final IRCService service) {
        super(service.getApplicationContext());
        mService = service;
    }

    // Server stuff
    @Override
    public void onConnect(final ConnectEvent<PircBotX> event) {
        event.getBot().setStatus(mService.getString(R.string.status_connected));

        event.getBot().appendToBuffer(EventParser.getOutputForEvent(event, mService));
    }

    // This HAS to be an unexpected disconnect. If it isn't then there's something wrong.
    @Override
    public void onDisconnect(final DisconnectEvent<PircBotX> event) {
        event.getBot().setStatus(mService.getString(R.string.status_disconnected));

        event.getBot().appendToBuffer(EventParser.getOutputForEvent(event, mService));

        mService.onUnexpectedDisconnect(event.getBot().getConfiguration().getTitle());
    }

    @Override
    public void onNotice(final NoticeEvent<PircBotX> event) {
        if (event.getChannel() == null) {
            if (event.getUser().getBuffer().isEmpty()) {
                event.getBot().appendToBuffer(EventParser.getOutputForEvent(event, mService));
            } else {
                event.getUser().appendToBuffer(EventParser.getOutputForEvent(event, mService));
            }
        } else {
            event.getChannel().appendToBuffer(EventParser.getOutputForEvent(event, mService));
        }
    }

    @Override
    public void onIOException(final IOExceptionEvent<PircBotX> event) {
        event.getBot().setStatus(mService.getString(R.string.status_disconnected));

        event.getBot().appendToBuffer(EventParser.getOutputForEvent(event, mService));

        mService.onUnexpectedDisconnect(event.getBot().getConfiguration().getTitle());
    }

    // Channel stuff
    @Override
    public void onBotJoin(final JoinEvent<PircBotX> event) {
        onChannelMessage(event, event.getChannel());
    }

    @Override
    public void onMessage(final MessageEvent<PircBotX> event) {
        super.onMessage(event);

        if (event.getMessage().contains(event.getBot().getNick())) {
            final String title = event.getBot().getConfiguration().getTitle();
            mService.mention(title, event.getChannel().getName());
        }
    }

    @Override
    public void onUserList(final UserListEvent<PircBotX> event) {
        onSetupChannelUserList(event.getChannel());
    }

    @Override
    public void onAction(final ActionEvent<PircBotX> event) {
        super.onAction(event);

        if (event.getChannel() != null && event.getMessage().contains(event.getBot().getNick())) {
                final String title = event.getBot().getConfiguration().getTitle();
                mService.mention(title, event.getChannel().getName());
        }
    }

    @Override
    public void onNickChangePerChannel(final NickChangeEventPerChannel<PircBotX> event) {
        onChannelMessage(event, event.getChannel());

        final ArrayList<String> set = event.getChannel().getUserList();
        final String oldFormattedNick = event.getOldNick();
        final String newFormattedNick = event.getNewNick();

        set.set(set.indexOf(oldFormattedNick), newFormattedNick);
        Collections.sort(set, new IRCUserComparator());
    }

    @Override
    public void onOtherUserJoin(final JoinEvent<PircBotX> event) {
        onChannelMessage(event, event.getChannel());

        final ArrayList<String> set = event.getChannel().getUserList();
        set.add(event.getUser().getPrettyNick(event.getChannel()));
        Collections.sort(set, new IRCUserComparator());
    }

    @Override
    public void onMode(final ModeEvent<PircBotX> event) {
        if (event.getUser() != null) {
            super.onMode(event);

            onSetupChannelUserList(event.getChannel());
        }
    }

    @Override
    public void onOtherUserPart(final PartEvent<PircBotX> event) {
        onOtherUserDepart(event, event.getChannel(), event.getUser());
    }

    @Override
    public void onQuitPerChannel(final QuitEventPerChannel<PircBotX> event) {
        onOtherUserDepart(event, event.getChannel(), event.getUser());
    }

    private void onOtherUserDepart(final Event<PircBotX> event, final Channel channel, final User user) {
        onChannelMessage(event, channel);

        final ArrayList<String> set = channel.getUserList();
        set.remove(user.getPrettyNick(channel));
        Collections.sort(set, new IRCUserComparator());
    }

    @Override
    void onPrivateEvent(final Event<PircBotX> event, final User user, final String message) {
        if (!message.equals("")) {
            onUserMessage(event, user);
        }

        if (!event.getBot().getUserChannelDao().getPrivateMessages().contains(user)) {
            user.createPrivateMessage();
        }

        if (!user.equals(event.getBot().getUserBot())) {
            final String title = event.getBot().getConfiguration().getTitle();
            mService.mention(title, user.getNick());
        }
    }

    private void onSetupChannelUserList(final Channel channel) {
        final ArrayList<String> userList = new ArrayList<>();

        for (final User user : channel.getUsers()) {
            userList.add(user.getPrettyNick(channel));
        }

        Collections.sort(userList, new IRCUserComparator());
        channel.setUserList(userList);
    }

    @Override
    public void onServerMessage(Event<PircBotX> event) {
        event.getBot().appendToBuffer(EventParser.getOutputForEvent(event, mService));
    }

    @Override
    void onChannelMessage(Event<PircBotX> event, Channel channel) {
        channel.appendToBuffer(EventParser.getOutputForEvent(event, mService));
    }

    @Override
    void onUserMessage(Event<PircBotX> event, User user) {
        user.appendToBuffer(EventParser.getOutputForEvent(event, mService));
    }
}