package com.koombea.testjorge.view;

import android.os.Bundle;

import com.koombea.testjorge.R;
import com.koombea.testjorge.common.ActivityBase;

public class PopUpActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_post);
        setControls();
    }

    @Override
    protected void setControls() {

    }
}
