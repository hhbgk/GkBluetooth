package com.gk.app.bluetooth.main;

import android.app.Application;

import com.gk.app.bluetooth.util.ToastUtil;
import com.gk.lib.bluetooth.BluetoothClient;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public final class App extends Application {
    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        ToastUtil.init(this);
        BluetoothClient.initialize(this);
    }

    public static App getApplication() {
        return sApp;
    }
}
