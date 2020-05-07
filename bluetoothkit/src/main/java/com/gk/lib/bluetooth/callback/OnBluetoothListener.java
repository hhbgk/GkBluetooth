package com.gk.lib.bluetooth.callback;

import android.bluetooth.BluetoothDevice;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public abstract class OnBluetoothListener implements ScanningCallback, SwitchStateCallback,
        ConnectionStateCallback, BondSateCallback, AclStateCallback {
    @Override
    public void onAclConnected(BluetoothDevice device) {

    }

    @Override
    public void onAclDisconnected(BluetoothDevice device) {

    }

    @Override
    public void onBondStateChanged(BluetoothDevice device, int state) {

    }

    @Override
    public void onConnectionStateChanged(BluetoothDevice device, int state) {

    }

    @Override
    public void onScanningStart() {

    }

    @Override
    public void onScanningStop() {

    }

    @Override
    public void onFound(BluetoothDevice device) {

    }

    @Override
    public void onSwitchStateChanged(int state) {

    }
}
