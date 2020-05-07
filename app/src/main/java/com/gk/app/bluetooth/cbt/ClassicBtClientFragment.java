package com.gk.app.bluetooth.cbt;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gk.app.bluetooth.R;
import com.gk.app.bluetooth.adapter.DeviceAdapter;
import com.gk.app.bluetooth.base.BaseFragment;
import com.gk.app.bluetooth.util.GLog;
import com.gk.lib.bluetooth.BluetoothClient;
import com.gk.lib.bluetooth.callback.OnBluetoothListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassicBtClientFragment extends BaseFragment {
    private RecyclerView rv;
    private TextView tvTips;
    private EditText etInputMsg;
    private EditText etInputFile;
    private TextView tvLogs;
    private final DeviceAdapter mBtDevAdapter = new DeviceAdapter();

    public ClassicBtClientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classic_bt_client, container, false);
        rv = view.findViewById(R.id.rv_bt);
        tvTips = view.findViewById(R.id.tv_tips);
        etInputMsg = view.findViewById(R.id.input_msg);
        etInputFile = view.findViewById(R.id.input_file);
        tvLogs = view.findViewById(R.id.tv_log);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(mBtDevAdapter);

//        mReceiver = new ClassicBtReceiver(App.getApplication(), this);//注册蓝牙广播
//        BluetoothAdapter.getDefaultAdapter().startDiscovery();
        BluetoothClient.getInstance().startScanning();
        BluetoothClient.getInstance().registerBluetoothListener(onBluetoothListener);
    }

    private final OnBluetoothListener onBluetoothListener = new OnBluetoothListener() {
        @Override
        public void onAclConnected(BluetoothDevice device) {
            GLog.i(tag, "onAclConnected:" + device.getName());
        }

        @Override
        public void onAclDisconnected(BluetoothDevice device) {
            GLog.i(tag, "onAclDisconnected:" + device.getName());
        }

        @Override
        public void onBondStateChanged(BluetoothDevice device, int state) {
            GLog.i(tag, "onBondStateChanged:" + device.getName() + ", " + state);
        }

        @Override
        public void onConnectionStateChanged(BluetoothDevice device, int state) {
            GLog.i(tag, "onConnectionStateChanged:" + device.getName() + ", " + state);
        }

        @Override
        public void onScanningStart() {
            GLog.i(tag, "onScanningStart");
        }

        @Override
        public void onScanningStop() {
            GLog.i(tag, "onScanningStop");
        }

        @Override
        public void onFound(BluetoothDevice device) {
            GLog.i(tag, "onFound:" + device.getName());
        }

        @Override
        public void onSwitchStateChanged(int state) {
            GLog.i(tag, "onSwitchStateChanged:" + state);
        }
    };
}
