package com.gk.lib.bluetooth.callback;

import android.bluetooth.BluetoothDevice;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public interface BondSateCallback {
    void onBondStateChanged(BluetoothDevice device, int state);
}
