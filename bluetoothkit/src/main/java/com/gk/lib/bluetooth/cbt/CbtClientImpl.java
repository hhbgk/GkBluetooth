package com.gk.lib.bluetooth.cbt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.gk.lib.bluetooth.IBluetooth;
import com.gk.lib.bluetooth.callback.OnBluetoothListener;
import com.gk.lib.bluetooth.engine.ThreadPool;
import com.gk.lib.bluetooth.engine.listener.ConnectDeviceListener;
import com.gk.lib.bluetooth.engine.runnable.BtConnectionRunnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Des:经典蓝牙管理类
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public final class CbtClientImpl implements IBluetooth {
    private String tag = getClass().getSimpleName();
    private BluetoothReceiver mBtReceiver;
    private Context context;
    private ThreadPool threadPool;
    private BluetoothSocket bluetoothSocket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public CbtClientImpl(Context context) {
        this.context = context;
        mBtReceiver = new BluetoothReceiver();
        threadPool = new ThreadPool();
        //注册蓝牙广播
        context.registerReceiver(mBtReceiver, mBtReceiver.getIntentFilter());
    }

    @Override
    public Set<BluetoothDevice> getBondedDevices() {
        return BluetoothAdapter.getDefaultAdapter().getBondedDevices();
    }

    @Override
    public void connect(BluetoothDevice device) {
        Log.i(tag, "connect:" + device.getName());
        BluetoothSocket socket = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (socket == null) {
            return;
        }

        BtConnectionRunnable runnable = new BtConnectionRunnable(socket, new ConnectDeviceListener() {
            @Override
            public void onConnectDeviceSuccess(BluetoothSocket socket) {
                bluetoothSocket = socket;
                try {
                    inputStream = new DataInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    outputStream = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onConnectDeviceFailure(BluetoothSocket socket, String msg) {
                if (socket !=null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadPool.submit(runnable);
    }

    @Override
    public void disconnect(BluetoothDevice device) {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isConnected(BluetoothDevice device) {
        boolean connected = (bluetoothSocket != null && bluetoothSocket.isConnected());
        if (device == null) return connected;
        return connected && bluetoothSocket.getRemoteDevice().equals(device);
    }

    @Override
    public void startScanning() {
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
    }

    @Override
    public void stopScanning() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
    }

    @Override
    public void registerBluetoothListener(OnBluetoothListener listener) {
        mBtReceiver.setOnBluetoothListener(listener);
    }

    @Override
    public void unregisterBluetoothListener(OnBluetoothListener listener) {
        mBtReceiver.setOnBluetoothListener(null);
    }

    @Override
    public void destroy() {
        if (context != null) {
            context.unregisterReceiver(mBtReceiver);
        }
        if (mBtReceiver.isScanning()) {
            stopScanning();
        }
        if (threadPool != null) threadPool.stop();
    }
}
