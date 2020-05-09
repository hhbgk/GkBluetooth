package com.gk.lib.bluetooth.core.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;

import com.gk.lib.bluetooth.IBluetooth;
import com.gk.lib.bluetooth.callback.OnBluetoothListener;
import com.gk.lib.bluetooth.core.SimpleBluetoothListener;
import com.gk.lib.bluetooth.util.HandlerUtil;

import java.util.List;
import java.util.Set;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-9
 * UpdateRemark:
 */
public final class BleClientImpl implements IBluetooth {
    private Context context;
    private boolean isScanning = false;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothGatt bluetoothGatt;
    private OnBluetoothListener onBluetoothListener = new SimpleBluetoothListener();

    public BleClientImpl(Context context) {
        this.context = context;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    @Override
    public Set<BluetoothDevice> getBondedDevices() {
        return null;
    }

    @Override
    public void connect(BluetoothDevice device) {
        bluetoothGatt = device.connectGatt(context, false, bluetoothGattCallback);
    }

    @Override
    public void disconnect(BluetoothDevice device) {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
    }

    @Override
    public boolean isConnected(BluetoothDevice device) {
        return false;
    }

    @Override
    public void startScanning() {
        isScanning = true;
        bluetoothLeScanner.startScan(scanCallback);
        HandlerUtil.remove(delayToCancel);
        HandlerUtil.postDelayed(delayToCancel, 3000);
    }

    @Override
    public void stopScanning() {
        if (isScanning) {
            isScanning = false;
            bluetoothLeScanner.stopScan(scanCallback); //停止扫描
        }
    }

    @Override
    public void registerBluetoothListener(OnBluetoothListener listener) {
        onBluetoothListener = listener;
    }

    @Override
    public void unregisterBluetoothListener(OnBluetoothListener listener) {
        onBluetoothListener = listener;
    }

    @Override
    public void destroy() {
        stopScanning();
        disconnect(null);
    }

    private final Runnable delayToCancel = new Runnable() {
        @Override
        public void run() {
            stopScanning();
        }
    };

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            onBluetoothListener.onFound(result.getDevice());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
        }

        @Override
        public void onScanFailed(int errorCode) {
        }
    };

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        //连接状态改变时回调
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

         //UUID搜索成功回调
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        //写入成功回调函数
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }
    };
}
