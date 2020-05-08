package com.gk.lib.bluetooth.engine.listener;

import android.bluetooth.BluetoothSocket;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-8
 * UpdateRemark:
 */
public interface ConnectDeviceListener {
    void onConnectDeviceSuccess(BluetoothSocket socket);
    void onConnectDeviceFailure(BluetoothSocket socket, String msg);
}
