package com.koombea.testjorge.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.koombea.testjorge.R;

public final class CustomProgressBar {

    private Dialog mDialog;

    public Dialog show(Context context) {
        return show(context, null);
    }

    public Dialog show(Context context, CharSequence title) {
        return show(context, title, false);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable) {
        return show(context, title, cancelable, null);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable,
                       DialogInterface.OnCancelListener cancelListener) {
        LayoutInflater inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflator != null;
        final View view = inflator.inflate(R.layout.progress_bar, null);
        if(title != null) {
            final TextView tv = view.findViewById(R.id.id_title);
            tv.setText(title);
        }

        mDialog = new Dialog(context, R.style.CustomProgressBarStyle);
        mDialog.setContentView(view);
        mDialog.setCancelable(cancelable);
        mDialog.setOnCancelListener(cancelListener);
        mDialog.show();

        return mDialog;
    }

    public Dialog getDialog() {
        return mDialog;
    }
}
