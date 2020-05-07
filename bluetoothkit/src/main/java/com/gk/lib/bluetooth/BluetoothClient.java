package com.gk.lib.bluetooth;

import android.content.Context;

import com.gk.lib.bluetooth.callback.OnBluetoothListener;
import com.gk.lib.bluetooth.cbt.CbtClientImpl;

import java.lang.ref.SoftReference;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public final class BluetoothClient implements IBluetooth {
    private static volatile BluetoothClient sInstance = null;
    private IBluetooth client;
    private static SoftReference<Context> softReference;

    public static BluetoothClient getInstance() {
        if (null == sInstance) {
            synchronized (BluetoothClient.class) {
                if (sInstance == null) {
                    sInstance = new BluetoothClient();
                }
            }
        }
        return sInstance;
    }

    public static void initialize(Context context) {
        softReference = new SoftReference<>(context);
    }

    private BluetoothClient() {
        if (softReference == null || softReference.get() == null) {
            throw new IllegalStateException("Invoke initialize() first!");
        }
        client = new CbtClientImpl(softReference.get());
    }

    @Override
    public void startScanning() {
        client.startScanning();
    }

    @Override
    public void stopScanning() {
        client.stopScanning();
    }

    @Override
    public void registerBluetoothListener(OnBluetoothListener listener) {
        client.registerBluetoothListener(listener);
    }

    @Override
    public void unregisterBluetoothListener(OnBluetoothListener listener) {
        client.unregisterBluetoothListener(listener);
    }

    @Override
    public void destroy() {
        client.destroy();
        softReference.clear();
    }
}
