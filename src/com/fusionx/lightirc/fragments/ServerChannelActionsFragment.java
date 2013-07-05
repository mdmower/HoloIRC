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

package com.fusionx.lightirc.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import com.fusionx.lightirc.R;
import com.fusionx.lightirc.activity.IRCFragmentActivity;
import com.fusionx.lightirc.adapters.ActionsArrayAdapter;
import com.fusionx.lightirc.promptdialogs.ChannelNamePromptDialog;
import com.fusionx.lightirc.promptdialogs.NickPromptDialog;
import com.fusionx.lightirc.service.IRCService;
import org.pircbotx.PircBotX;

public class ServerChannelActionsFragment extends ListFragment implements AdapterView.OnItemClickListener {
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String[] values = getResources().getStringArray(R.array.actions);
        final ActionsArrayAdapter adapter = new ActionsArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        final IRCService service = ((IRCFragmentActivity) getActivity()).getService();
        final PircBotX bot = service.getBot(((IRCFragmentActivity) getActivity()).getBuilder().getTitle());
        switch (i) {
            case 0:
                channelNameDialog(bot);
                break;
            case 1:
                nickChangeDialog(bot);
                break;
            case 2:
                ((IRCFragmentActivity) getActivity()).disconnect();
                break;
        }
    }

    private void nickChangeDialog(final PircBotX bot) {
        final NickPromptDialog nickDialog = new NickPromptDialog(getActivity(), bot.getNick()) {
            @Override
            public void onOkClicked(final String input) {
                final DialogTask ChangeNickTask = new DialogTask() {
                    @Override
                    protected Void doInBackground(Void... objects) {
                        bot.sendIRC().changeNick(input);
                        return null;
                    }
                };
                ChangeNickTask.execute();
                ((IRCFragmentActivity) getActivity()).closeAllSlidingMenus();
            }
        };
        nickDialog.show();
    }

    private void channelNameDialog(final PircBotX bot) {
        final ChannelNamePromptDialog dialog = new ChannelNamePromptDialog(getActivity()) {
            @Override
            public void onOkClicked(final String input) {
                final DialogTask JoinTask = new DialogTask() {
                    @Override
                    protected Void doInBackground(Void... objects) {
                        bot.sendIRC().joinChannel(input);
                        return null;
                    }
                };
                JoinTask.execute();
                ((IRCFragmentActivity) getActivity()).closeAllSlidingMenus();
            }
        };
        dialog.show();
    }

    private abstract class DialogTask extends AsyncTask<Void, Void, Void> {
    }
}