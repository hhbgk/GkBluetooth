package com.gk.lib.bluetooth.core.cbt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.gk.lib.bluetooth.IBluetooth;
import com.gk.lib.bluetooth.bean.BluetoothInfo;
import com.gk.lib.bluetooth.callback.OnBluetoothListener;
import com.gk.lib.bluetooth.core.AbstractBluetooth;
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
public final class CbtClientImpl extends AbstractBluetooth {
    private BluetoothReceiver mBtReceiver;
    private Context context;
    private ThreadPool threadPool;
    private BluetoothSocket bluetoothSocket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private DataReceivedTask dataReceivedTask;
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
        if (isConnected(device)) {
            Log.w(tag, device.getName() + " has connected!");
            return;
        }
        BluetoothSocket socket = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (socket == null) {
            return;
        }
        BluetoothInfo info = new BluetoothInfo(socket, device);
        BtConnectionRunnable runnable = new BtConnectionRunnable(info, new ConnectDeviceListener() {
            @Override
            public void onConnectDeviceSuccess(BluetoothInfo bluetoothInfo) {
                bluetoothSocket = bluetoothInfo.socket;
                try {
                    inputStream = new DataInputStream(bluetoothInfo.socket.getInputStream());
                    if (dataReceivedTask != null) dataReceivedTask.stopRunning();
                    dataReceivedTask = new DataReceivedTask(inputStream, mBtReceiver.getBluetoothListener());
                    dataReceivedTask.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    outputStream = new DataOutputStream(bluetoothInfo.socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mBtReceiver.getBluetoothListener().onConnectDeviceSuccess(bluetoothInfo.device);
            }

            @Override
            public void onConnectDeviceFailure(BluetoothInfo bluetoothInfo, String msg) {
                if (bluetoothInfo.socket !=null) {
                    try {
                        bluetoothInfo.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mBtReceiver.getBluetoothListener().onConnectDeviceFailure(bluetoothInfo.device, msg);
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
    public void tryToSend(byte[] data) {
        if (outputStream != null) {
            try {
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(tag, "No output stream");
        }
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
        if (dataReceivedTask != null) dataReceivedTask.stopRunning();
    }
}
