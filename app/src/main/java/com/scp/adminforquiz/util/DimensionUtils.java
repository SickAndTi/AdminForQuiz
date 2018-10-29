package com.scp.adminforquiz.util;

import android.content.Context;
import android.util.TypedValue;

import com.scp.adminforquiz.Constants;

import toothpick.Toothpick;

public class DimensionUtils {
    public static int convertDpToPixels(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Toothpick.openScope(Constants.APP_SCOPE).getInstance(Context.class).getResources().getDisplayMetrics());
    }
}
