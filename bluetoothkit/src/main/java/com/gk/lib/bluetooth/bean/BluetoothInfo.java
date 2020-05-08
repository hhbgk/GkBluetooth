package com.gk.lib.bluetooth.bean;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-8
 * UpdateRemark:
 */
public final class BluetoothInfo {
    public final BluetoothSocket socket;
    public final BluetoothDevice device;

    public BluetoothInfo(BluetoothSocket socket, BluetoothDevice device) {
        this.socket = socket;
        this.device = device;
    }
}
