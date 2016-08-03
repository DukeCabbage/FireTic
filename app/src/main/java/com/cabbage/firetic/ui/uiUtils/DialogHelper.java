package com.cabbage.firetic.ui.uiUtils;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogHelper {

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String message) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public static ProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
