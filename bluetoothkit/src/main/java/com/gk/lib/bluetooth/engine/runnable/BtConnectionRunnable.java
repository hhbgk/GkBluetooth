package com.gk.lib.bluetooth.engine.runnable;

import android.bluetooth.BluetoothSocket;

import com.gk.lib.bluetooth.bean.BluetoothInfo;
import com.gk.lib.bluetooth.engine.listener.ConnectDeviceListener;

import java.io.IOException;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-8
 * UpdateRemark:
 */
public final class BtConnectionRunnable extends AbstractRunnable {
    private BluetoothSocket socket;
    private BluetoothInfo info;
    private ConnectDeviceListener listener;

    public BtConnectionRunnable(BluetoothInfo info, ConnectDeviceListener listener) {
        this.socket = info.socket;
        this.info = info;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            if (!socket.isConnected()) socket.connect();
            listener.onConnectDeviceSuccess(info);
        } catch (IOException e) {
            listener.onConnectDeviceFailure(info, e.getMessage());
            e.printStackTrace();
        }
    }
}
