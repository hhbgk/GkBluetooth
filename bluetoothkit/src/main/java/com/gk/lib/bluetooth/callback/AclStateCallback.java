package com.gk.lib.bluetooth.callback;

import android.bluetooth.BluetoothDevice;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public interface AclStateCallback {
    void onAclConnected(BluetoothDevice device);
    void onAclDisconnected(BluetoothDevice device);
}
