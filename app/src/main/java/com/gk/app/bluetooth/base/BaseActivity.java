package com.gk.app.bluetooth.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.gk.app.bluetooth.main.App;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public abstract class BaseActivity extends FragmentActivity {
    protected App mApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = App.getApplication();
    }
}
