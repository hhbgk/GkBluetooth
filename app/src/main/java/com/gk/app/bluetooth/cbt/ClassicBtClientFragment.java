package com.gk.app.bluetooth.cbt;

import android.bluetooth.BluetoothDevice;
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
import com.gk.app.bluetooth.util.GLog;
import com.gk.lib.bluetooth.BluetoothClient;
import com.gk.lib.bluetooth.callback.OnBluetoothListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassicBtClientFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView rv;
    private TextView tvTips;
    private EditText etInputMsg;
    private EditText etInputFile;
    private TextView tvLogs;
    private Button btnRescan;

    private final DeviceAdapter mAdapter = new DeviceAdapter();

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
        btnRescan = view.findViewById(R.id.btn_rescan);
        btnRescan.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(mAdapter);
        mAdapter.addAll(BluetoothClient.getInstance().getBondedDevices());
        BluetoothClient.getInstance().startScanning();
        BluetoothClient.getInstance().registerBluetoothListener(onBluetoothListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BluetoothClient.getInstance().destroy();
    }

    @Override
    public void onClick(View v) {
        if (v == btnRescan) {

        }
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
            GLog.i(tag, "onFound:" + device.getName() + ":" + device.getAddress());
            mAdapter.add(device);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSwitchStateChanged(int state) {
            GLog.i(tag, "onSwitchStateChanged:" + state);
        }
    };

    private final OnItemClickListener<BluetoothDevice> onItemClickListener
            = new OnItemClickListener<BluetoothDevice>() {
        @Override
        public void onItemClick(View view, BluetoothDevice device, int position) {
            GLog.e(tag, "onItemClick:" + BluetoothClient.getInstance().isConnected(device));
            BluetoothClient.getInstance().connect(device);
        }

        @Override
        public void onItemLongClick(View view, BluetoothDevice device, int position) {
        }
    };
}
