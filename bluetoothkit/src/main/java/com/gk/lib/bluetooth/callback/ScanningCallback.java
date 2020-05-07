package com.gk.lib.bluetooth.callback;

import android.bluetooth.BluetoothDevice;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public interface ScanningCallback {
    void onScanningStart();
    void onScanningStop();
    void onFound(BluetoothDevice device);
}
