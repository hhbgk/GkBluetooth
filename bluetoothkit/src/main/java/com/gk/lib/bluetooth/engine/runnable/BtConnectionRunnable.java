package com.gk.lib.bluetooth.engine.runnable;

import android.bluetooth.BluetoothSocket;

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
    private ConnectDeviceListener listener;

    public BtConnectionRunnable(BluetoothSocket socket, ConnectDeviceListener listener) {
        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            if (!socket.isConnected()) socket.connect();
            listener.onConnectDeviceSuccess(socket);
        } catch (IOException e) {
            listener.onConnectDeviceFailure(socket, e.getMessage());
            e.printStackTrace();
        }
    }
}
