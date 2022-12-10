package com.planitse2022.planit.util.dpconverter;

import android.content.Context;
import android.util.TypedValue;

public class DPConverter {
    public static int dpToInt(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
