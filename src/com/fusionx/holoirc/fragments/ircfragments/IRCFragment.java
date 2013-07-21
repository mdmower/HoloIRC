/*
    HoloIRC - an IRC client for Android

    Copyright 2013 Lalit Maganti

    This file is part of HoloIRC.

    HoloIRC is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HoloIRC is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HoloIRC. If not, see <http://www.gnu.org/licenses/>.
 */

package com.fusionx.holoirc.fragments.ircfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import com.fusionx.holoirc.R;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class IRCFragment extends Fragment implements TextView.OnEditorActionListener {
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PROTECTED)
    private String title = null;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private TextView textView = null;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private EditText editText = null;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflate,
                             final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflate.inflate(R.layout.fragment_irc, container, false);

        setTextView((TextView) rootView.findViewById(R.id.textview));
        setEditText((EditText) rootView.findViewById(R.id.editText1));

        getEditText().setOnEditorActionListener(this);

        setTitle(getArguments().getString("title"));

        return rootView;
    }

    public void appendToTextView(final String text) {
        getTextView().append(Html.fromHtml(text.replace("\n", "<br/>")));
        Linkify.addLinks(getTextView(), Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
    }

    void writeToTextView(final String text) {
        getTextView().setText(Html.fromHtml(text.replace("\n", "<br/>")));
        Linkify.addLinks(getTextView(), Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        final ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollview);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void disableEditText() {
        editText.setEnabled(false);
    }
}