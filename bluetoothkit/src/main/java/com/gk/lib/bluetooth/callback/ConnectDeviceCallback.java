package com.gk.lib.bluetooth.callback;

import android.bluetooth.BluetoothDevice;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-8
 * UpdateRemark:
 */
public interface ConnectDeviceCallback {
    void onConnectDeviceSuccess(BluetoothDevice device);
    void onConnectDeviceFailure(BluetoothDevice device, String msg);
}
