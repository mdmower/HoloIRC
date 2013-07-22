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

package com.fusionx.lightirc.irc;

import android.content.Context;
import com.fusionx.lightirc.misc.Utils;

import java.util.HashMap;
import java.util.HashSet;

public class LightManager extends HashMap<String, LightThread> {
    private static final long serialVersionUID = 2426166268063489300L;

    private final Context applicationContext;

    public LightManager(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void disconnectAll() {
        final HashSet<LightThread> set = new HashSet<>(values());
        for (final LightThread bot : set) {
            bot.getBot().sendIRC().quitServer(Utils.getQuitReason(applicationContext));
            bot.getBot().shutdown();
        }
    }
}