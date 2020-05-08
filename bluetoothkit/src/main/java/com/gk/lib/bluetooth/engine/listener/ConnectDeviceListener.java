package com.gk.lib.bluetooth.engine.listener;

import com.gk.lib.bluetooth.bean.BluetoothInfo;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-8
 * UpdateRemark:
 */
public interface ConnectDeviceListener {
    void onConnectDeviceSuccess(BluetoothInfo bluetoothInfo);
    void onConnectDeviceFailure(BluetoothInfo bluetoothInfo, String msg);
}
