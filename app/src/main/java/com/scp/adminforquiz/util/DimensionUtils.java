package com.scp.adminforquiz.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;

import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.R;

import toothpick.Toothpick;

public class DimensionUtils {

    public static int convertDpToPixels(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Toothpick.openScope(Constants.APP_SCOPE).getInstance(Context.class).getResources().getDisplayMetrics());
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getActionBarHeight(FragmentActivity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
           return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        } else {
           return  0;
        }
    }
}
