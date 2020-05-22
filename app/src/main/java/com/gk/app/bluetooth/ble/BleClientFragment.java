package com.gk.app.bluetooth.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gk.app.bluetooth.R;
import com.gk.app.bluetooth.adapter.DeviceAdapter;
import com.gk.app.bluetooth.base.BaseFragment;
import com.gk.app.bluetooth.listener.OnItemClickListener;
import com.gk.app.bluetooth.util.GkLog;
import com.gk.app.bluetooth.util.ToastUtil;
import com.gk.lib.bluetooth.BluetoothClient;
import com.gk.lib.bluetooth.callback.OnBluetoothListener;
import com.gk.lib.bluetooth.util.HandlerUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class BleClientFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView rv;
    private EditText etWriteData;
    private TextView mTips;
    private Button btnRescan;
    private Button btnWrite;
    private final DeviceAdapter mAdapter = new DeviceAdapter();

    public BleClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ble_client, container, false);
        rv = v.findViewById(R.id.rv_ble);
        btnRescan = v.findViewById(R.id.btn_scan);
        btnRescan.setOnClickListener(this);
        btnWrite = v.findViewById(R.id.btn_write);
        btnWrite.setOnClickListener(this);
        etWriteData = v.findViewById(R.id.et_write);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(mAdapter);
        mAdapter.addAll(BluetoothClient.getInstance().getBondedDevices());
        BluetoothClient.getInstance().registerBluetoothListener(onBluetoothListener);
        BluetoothClient.getInstance().startScanning();
    }

    @Override
    public void onStop() {
        super.onStop();
        BluetoothClient.getInstance().destroy();
    }

    @Override
    public void onClick(View v) {
        if (v == btnRescan) {
            BluetoothClient.getInstance().startScanning();
        } else if (v == btnWrite) {
            String text = etWriteData.getText().toString();
            BluetoothClient.getInstance().tryToSend(text.getBytes());
        }
    }

    private final OnBluetoothListener onBluetoothListener = new OnBluetoothListener() {

        @Override
        public void onAclConnected(BluetoothDevice device) {
            GkLog.i(tag, "onAclConnected:" + device.getName());
        }

        @Override
        public void onAclDisconnected(BluetoothDevice device) {
            GkLog.i(tag, "onAclDisconnected:" + device.getName());
        }

        @Override
        public void onBondStateChanged(BluetoothDevice device, int state) {
            GkLog.i(tag, "onBondStateChanged:" + device.getName() + ", " + (state==BluetoothDevice.BOND_BONDED));
        }

        @Override
        public void onConnectionStateChanged(BluetoothDevice device, int state) {
            GkLog.i(tag, "onConnectionStateChanged:" + device.getName() + ", " + state);
        }

        @Override
        public void onScanningStart() {
            GkLog.i(tag, "onScanningStart");
        }

        @Override
        public void onScanningStop() {
            GkLog.i(tag, "onScanningStop");
        }

        @Override
        public void onFound(BluetoothDevice device) {
            GkLog.i(tag, "onFound:" + device.getName() + ", type=" + device.getType());
            mAdapter.add(device);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSwitchStateChanged(int state) {
            GkLog.i(tag, "onSwitchStateChanged:" + state);
        }

        @Override
        public void onSent(final byte[] data) {
            HandlerUtil.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    ToastUtil.showToastLong("Successful sent： " + new String(data));
                }
            });
        }

        @Override
        public void onConnectDeviceSuccess(final BluetoothDevice device) {
            boolean bondState = device.getBondState() == BluetoothDevice.BOND_BONDED;
            GkLog.i(tag, "onConnectDeviceSuccess:" + device.getName() + ", " + bondState);

            HandlerUtil.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    ToastUtil.showToastLong("Successful connected " + device.getName());
                }
            });
        }

        @Override
        public void onConnectDeviceFailure(BluetoothDevice device, String msg) {
            GkLog.e(tag, "onConnectDeviceFailure:" + device.getName() + ", " + msg);
        }
    };

    private final OnItemClickListener<BluetoothDevice> onItemClickListener = new OnItemClickListener<BluetoothDevice>() {
        @Override
        public void onItemClick(View view, BluetoothDevice device, int position) {
            GkLog.e(tag, "onItemClick:" + BluetoothClient.getInstance().isConnected(device)
                    + ", getType=" + device.getType());
            BluetoothClient.getInstance().connect(device);
        }

        @Override
        public void onItemLongClick(View view, BluetoothDevice o, int position) {

        }
    };
}
