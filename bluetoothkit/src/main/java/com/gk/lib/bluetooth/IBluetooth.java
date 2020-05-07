package com.gk.lib.bluetooth;

import com.gk.lib.bluetooth.callback.OnBluetoothListener;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public interface IBluetooth {
    void startScanning();
    void stopScanning();
    void registerBluetoothListener(OnBluetoothListener listener);
    void unregisterBluetoothListener(OnBluetoothListener listener);
    void destroy();
}
