package com.gk.lib.bluetooth.cbt;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.gk.lib.bluetooth.callback.OnBluetoothListener;

import static android.bluetooth.BluetoothAdapter.STATE_OFF;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public final class BluetoothReceiver extends BroadcastReceiver {
    private String tag = getClass().getSimpleName();
    private OnBluetoothListener onBluetoothListener = new SimpleBluetoothListener();
    private boolean isScanning = false;

    public IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        //蓝牙开关状态
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //蓝牙开始搜索
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //蓝牙搜索结束
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        //蓝牙发现新设备(未配对)
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //设备配对状态改变
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //设备建立连接
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        //设备断开连接
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        //BluetoothAdapter连接状态
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        //BluetoothHeadset连接状态
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        //BluetoothA2dp连接状态
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);

//        context.registerReceiver(this, filter);
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        BluetoothDevice device;
        int state;
        switch (action) {
            /*
             * 蓝牙开关状态
             * int STATE_OFF = 10; //蓝牙关闭
             * int STATE_ON = 12; //蓝牙打开
             * int STATE_TURNING_OFF = 13; //蓝牙正在关闭
             * int STATE_TURNING_ON = 11; //蓝牙正在打开
             */
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, STATE_OFF);
                onBluetoothListener.onSwitchStateChanged(state);
                break;
            /*
             * 蓝牙开始搜索
             */
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                isScanning = true;
                onBluetoothListener.onScanningStart();
                break;
            /*
             * 蓝牙搜索结束
             */
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                isScanning = false;
                onBluetoothListener.onScanningStop();
                break;
            /*
             * 发现新设备
             */
            case BluetoothDevice.ACTION_FOUND:
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onBluetoothListener.onFound(device);
                break;
            /*
             * 设备配对状态改变
             * int BOND_NONE = 10; //配对没有成功
             * int BOND_BONDING = 11; //配对中
             * int BOND_BONDED = 12; //配对成功
             */
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
                onBluetoothListener.onBondStateChanged(device, state);
                break;
            /*
             * 设备建立连接
             * int STATE_DISCONNECTED = 0; //未连接
             * int STATE_CONNECTING = 1; //连接中
             * int STATE_CONNECTED = 2; //连接成功
             */
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onBluetoothListener.onAclConnected(device);
                break;
            /*
             * 设备断开连接
             */
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onBluetoothListener.onAclDisconnected(device);
                break;
            /*
             * 本地蓝牙适配器
             * BluetoothAdapter连接状态
             */
            case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED);
                onBluetoothListener.onConnectionStateChanged(device, state);
                break;
            /*
             * 提供用于手机的蓝牙耳机支持
             * BluetoothHeadset连接状态
             */
            case BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED:
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, BluetoothHeadset.STATE_AUDIO_DISCONNECTED);
                onBluetoothListener.onConnectionStateChanged(device, state);
                break;
            /*
             * 定义高质量音频可以从一个设备通过蓝牙连接传输到另一个设备
             * BluetoothA2dp连接状态
             */
            case BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED:
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_DISCONNECTED);
                onBluetoothListener.onConnectionStateChanged(device, state);
                break;
            default:
                break;
        }
    }

    public boolean isScanning() {
        return isScanning;
    }

    public void setOnBluetoothListener(OnBluetoothListener listener) {
        onBluetoothListener = listener;
    }
}
