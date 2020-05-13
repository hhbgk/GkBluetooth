package com.gk.lib.bluetooth.core.cbt;

import android.os.SystemClock;
import android.util.Log;

import com.gk.lib.bluetooth.callback.OnBluetoothListener;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-12
 * UpdateRemark:
 */
public final class DataReceivedTask extends Thread {
    private String tag = getClass().getSimpleName();
    private final DataInputStream inputStream;
    private final OnBluetoothListener onBluetoothListener;
    private boolean isRunning = false;

    DataReceivedTask(DataInputStream inputStream, OnBluetoothListener onBluetoothListener) {
        this.inputStream = inputStream;
        this.onBluetoothListener = onBluetoothListener;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                if (onBluetoothListener != null) {
                    byte[] buffer = new byte[1024];
                    if (inputStream.read(buffer) > 0) {
                        onBluetoothListener.onReceived(buffer);
                    } else {
                        Log.e(tag, "There is no more data!");
                        SystemClock.sleep(50);
                    }
                } else {
                    Log.e(tag, "No implement for OnBluetoothListener");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void stopRunning() {
        isRunning = false;
    }
}
