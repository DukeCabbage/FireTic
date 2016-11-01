package com.cabbage.firetic.ui.uiUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;

public class DialogHelper {

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String message) {
        dismissProgressDialog();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public static void showProgressDialog(Context context, @StringRes int messageRes) {
        String message;
        try {
            message = context.getString(messageRes);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return;
        }
        showProgressDialog(context, message);
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    public static ProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
