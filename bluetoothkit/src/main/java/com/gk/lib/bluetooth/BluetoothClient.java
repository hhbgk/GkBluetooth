package com.gk.lib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.gk.lib.bluetooth.callback.OnBluetoothListener;
import com.gk.lib.bluetooth.core.ble.BleClientImpl;
import com.gk.lib.bluetooth.core.cbt.CbtClientImpl;

import java.lang.ref.SoftReference;
import java.util.Set;

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
//        client = new CbtClientImpl(softReference.get());
        client = new BleClientImpl(softReference.get());
    }

    @Override
    public Set<BluetoothDevice> getBondedDevices() {
        return client.getBondedDevices();
    }

    @Override
    public void connect(BluetoothDevice device) {
        client.connect(device);
    }

    @Override
    public void disconnect(BluetoothDevice device) {
        client.disconnect(device);
    }

    @Override
    public boolean isConnected(BluetoothDevice device) {
        return client.isConnected(device);
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
    public void tryToSend(byte[] data) {
        client.tryToSend(data);
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
