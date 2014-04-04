package com.fusionx.lightirc.util;

import com.fusionx.lightirc.R;
import com.fusionx.lightirc.misc.AppPreferences;
import com.fusionx.relay.constants.Theme;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.widget.TextView;

public class UIUtils {

    private static Typeface mRobotoLightTypeface = null;

    private static Typeface mRobotoThinTypeface = null;

    private static boolean isTablet(final Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration
                .SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLandscape(final Context context) {
        return context.getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE;
    }

    public static Typeface getRobotoLight(final Context context) {
        if (mRobotoLightTypeface == null) {
            mRobotoLightTypeface = Typeface.createFromAsset(context.getAssets(),
                    "Roboto-Light.ttf");
        }
        return mRobotoLightTypeface;
    }

    public static void setRobotoLight(final Context context, final TextView textView) {
        final Typeface font = getRobotoLight(context);
        textView.setTypeface(font);
    }

    private static Typeface getRobotoThin(final Context context) {
        if (mRobotoLightTypeface == null) {
            mRobotoThinTypeface = Typeface.createFromAsset(context.getAssets(),
                    "Roboto-Thin.ttf");
        }
        return mRobotoThinTypeface;
    }

    public static void setRobotoThin(final Context context, final TextView textView) {
        final Typeface font = getRobotoThin(context);
        textView.setTypeface(font);
    }

    public static int getThemeInt() {
        return AppPreferences.theme != Theme.DARK ? R.style.Light : R.style.Dark;
    }

    public static void toggleSlidingPane(final SlidingPaneLayout slidingPaneLayout) {
        if (slidingPaneLayout.isOpen()) {
            slidingPaneLayout.closePane();
        } else {
            slidingPaneLayout.openPane();
        }
    }

    public static void toggleDrawerLayout(final DrawerLayout drawerLayout, final View drawer) {
        if (drawerLayout.isDrawerOpen(drawer)) {
            drawerLayout.closeDrawer(drawer);
        } else {
            drawerLayout.openDrawer(drawer);
        }
    }
}