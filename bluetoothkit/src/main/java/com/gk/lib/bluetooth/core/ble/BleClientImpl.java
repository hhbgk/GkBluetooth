package com.gk.lib.bluetooth.core.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.util.Log;

import com.gk.lib.bluetooth.callback.OnBluetoothListener;
import com.gk.lib.bluetooth.core.AbstractBluetooth;
import com.gk.lib.bluetooth.core.SimpleBluetoothListener;
import com.gk.lib.bluetooth.util.HandlerUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-9
 * UpdateRemark:
 */
public final class BleClientImpl extends AbstractBluetooth {
    /**
     * 设备服务UUID, 需固件配合同时修改
     */
    private static final UUID UUID_SERVICE = UUID.fromString("0000ae00-0000-1000-8000-00805f9b34fb");
    /**
     * 设备特征值UUID, 需固件配合同时修改
     */
    private static final UUID UUID_WRITE = UUID.fromString("0000ae01-0000-1000-8000-00805f9b34fb");  // 用于发送数据到设备
    private static final UUID UUID_NOTIFICATION = UUID.fromString("0000ae02-0000-1000-8000-00805f9b34fb"); // 用于接收设备推送的数据
    //    private static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private Context context;
    private boolean isScanning = false;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothGatt bluetoothGatt;
    private OnBluetoothListener onBluetoothListener = new SimpleBluetoothListener();
    private Map<UUID, Map<UUID, BluetoothGattCharacteristic>> mDeviceProfile = new HashMap<>();

    public BleClientImpl(Context context) {
        this.context = context;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    @Override
    public Set<BluetoothDevice> getBondedDevices() {
        return BluetoothAdapter.getDefaultAdapter().getBondedDevices();
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
        onBluetoothListener.onScanningStart();
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
    public void tryToSend(byte[] data) {
        BluetoothGattCharacteristic characteristic = bluetoothGatt.getService(UUID_SERVICE).getCharacteristic(UUID_WRITE);
        characteristic.setValue(data);
        bluetoothGatt.writeCharacteristic(characteristic);
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
        if (mDeviceProfile != null) mDeviceProfile.clear();
    }

    private final Runnable delayToCancel = new Runnable() {
        @Override
        public void run() {
            stopScanning();
            onBluetoothListener.onScanningStop();
        }
    };

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            onBluetoothListener.onFound(result.getDevice());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.i(tag, "onBatchScanResults " + results.size());
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(tag, "onScanFailed errorCode=" + errorCode);
        }
    };

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            Log.i(tag, "onPhyUpdate: status=" + status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            Log.i(tag, "onPhyRead: " + status);
        }

        //当连接上设备或者断开连接的时候会回调
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i(tag, "onConnectionStateChange: status=" + status + ", newState=" + newState);
            if (BluetoothProfile.STATE_CONNECTED == newState) {
                if (!bluetoothGatt.discoverServices()) {
                    Log.e(tag, "Cannot start discover services");
                }
                onBluetoothListener.onConnectDeviceSuccess(gatt.getDevice());
            } else if (BluetoothProfile.STATE_DISCONNECTED == newState) {
                onBluetoothListener.onConnectDeviceFailure(gatt.getDevice(), "Disconnected");
                disconnect(null);
            }
        }

        //当搜索到服务的时候回调
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.i(tag, "onServicesDiscovered: status=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                setCharacteristicNotification(gatt.getService(UUID_SERVICE).getCharacteristic(UUID_WRITE), true);
                refreshServiceProfile();

            } else {
                Log.e(tag, "onServicesDiscovered: failure");
            }
        }

        //读取设备Characteristic的时候回调
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(tag, "onCharacteristicRead: status=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                onBluetoothListener.onReceived(characteristic.getValue());
            }
        }

        //当向设备的写入Characteristic的时候调用
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(tag, "onCharacteristicWrite: status=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(tag, "onCharacteristicWrite: write success");
                onBluetoothListener.onSent(characteristic.getValue());
            }
        }

        //设备发出通知时会调用到该接口
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.i(tag, "onCharacteristicChanged ");
            onBluetoothListener.onReceived(characteristic.getValue());
        }

        //当向设备Descriptor中读取数据时，会回调该函数
        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.i(tag, "onDescriptorRead:status=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(tag, "onDescriptorRead: success");
                onBluetoothListener.onReceived(descriptor.getValue());
            }
        }

        //当向设备Descriptor中写数据时，会回调该函数
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.i(tag, "onDescriptorWrite: status=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(tag, "onDescriptorWrite: success");
            }
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            Log.i(tag, "onReliableWriteCompleted:status=" + status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.i(tag, "onReadRemoteRssi: rssi=" + rssi + ", status=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(tag, "onReadRemoteRssi: success");
            }
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            Log.i(tag, "onMtuChanged: mtu=" + mtu + ", status=" + status);
        }
    };


    private void refreshServiceProfile() {
        List<BluetoothGattService> services = bluetoothGatt.getServices();

        Map<UUID, Map<UUID, BluetoothGattCharacteristic>> newProfiles = new HashMap<>();

        for (BluetoothGattService service : services) {
            UUID serviceUUID = service.getUuid();
            Log.i(tag, "serviceUUID:" + serviceUUID.toString());
            Map<UUID, BluetoothGattCharacteristic> map = newProfiles.get(serviceUUID);

            if (map == null) {
                map = new HashMap<>();
                newProfiles.put(service.getUuid(), map);
            }

            List<BluetoothGattCharacteristic> characters = service.getCharacteristics();

            for (BluetoothGattCharacteristic character : characters) {
                if (setCharacteristicNotification(character)) {
                    Log.i(tag, "support Notification:" + character.getService().getUuid());
                } else {
                    Log.w(tag, "No Notification:" + character.getService().getUuid()
                            +", getUuid=" + character.getUuid());
                }
                UUID characterUUID = character.getUuid();
                map.put(characterUUID, character);
            }
        }

        mDeviceProfile.clear();
        mDeviceProfile.putAll(newProfiles);
    }

    private boolean isCharacteristicNotifiable(BluetoothGattCharacteristic characteristic) {
        return characteristic != null
                && (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }

    private boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic) {
        if (characteristic == null) {
            Log.e(tag, "characteristic not exist!");
            return false;
        }

        if (!isCharacteristicNotifiable(characteristic)) {
//            Log.e(tag, "characteristic not notifiable!");
            return false;
        }

        if (bluetoothGatt == null) {
            Log.e(tag, "ble gatt null");
            return false;
        }

        if (!bluetoothGatt.setCharacteristicNotification(characteristic, true)) {
            Log.e(tag, "setCharacteristicNotification failed");
            return false;
        }

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_NOTIFICATION);

        if (descriptor == null) {
            Log.e(tag, "getDescriptor for notify null!");
            return false;
        }

        if (!descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
            Log.e(tag, "setValue for notify descriptor failed!");
            return false;
        }

        if (!bluetoothGatt.writeDescriptor(descriptor)) {
            Log.e(tag, "writeDescriptor for notify failed");
            return false;
        }

        return true;
    }
}
