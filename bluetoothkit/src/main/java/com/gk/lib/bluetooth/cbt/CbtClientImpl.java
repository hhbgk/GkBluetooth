package com.gk.lib.bluetooth.cbt;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.gk.lib.bluetooth.IBluetooth;
import com.gk.lib.bluetooth.callback.OnBluetoothListener;

/**
 * Des:经典蓝牙管理类
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public final class CbtClientImpl implements IBluetooth {
    private BluetoothReceiver mBtReceiver;
    private Context context;

    public CbtClientImpl(Context context) {
        this.context = context;
        mBtReceiver = new BluetoothReceiver();
        //注册蓝牙广播
        context.registerReceiver(mBtReceiver, mBtReceiver.getIntentFilter());
    }

    @Override
    public void startScanning() {
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
    }

    @Override
    public void stopScanning() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
    }

    @Override
    public void registerBluetoothListener(OnBluetoothListener listener) {
        mBtReceiver.setOnBluetoothListener(listener);
    }

    @Override
    public void unregisterBluetoothListener(OnBluetoothListener listener) {
        mBtReceiver.setOnBluetoothListener(null);
    }

    @Override
    public void destroy() {
        if (context != null) {
            context.unregisterReceiver(mBtReceiver);
        }
        if (mBtReceiver.isScanning()) {
            stopScanning();
        }
    }
}
