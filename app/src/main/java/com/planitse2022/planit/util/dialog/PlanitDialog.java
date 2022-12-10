package com.planitse2022.planit.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public abstract class PlanitDialog {
    protected Context context;
    protected Dialog dialog;

    PlanitDialog(Context context) {
        this.context = context;
    }

    protected void dialogInit() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void dissmissDailog() {
        dialog.dismiss();
    }

    public abstract void show();
}
