package com.gk.lib.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.gk.lib.bluetooth.callback.OnBluetoothListener;

import java.util.Set;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public interface IBluetooth {
    Set<BluetoothDevice> getBondedDevices();
    void connect(BluetoothDevice device);
    void disconnect(BluetoothDevice device);
    boolean isConnected(BluetoothDevice device);
    void startScanning();
    void stopScanning();
    void registerBluetoothListener(OnBluetoothListener listener);
    void unregisterBluetoothListener(OnBluetoothListener listener);
    void destroy();
}
