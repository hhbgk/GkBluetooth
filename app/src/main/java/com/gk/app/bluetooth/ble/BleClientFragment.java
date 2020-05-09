package com.gk.app.bluetooth.ble;

import android.bluetooth.BluetoothGatt;
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
import com.gk.app.bluetooth.listener.OnItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class BleClientFragment extends BaseFragment {
    private RecyclerView rv;
    private EditText mWriteET;
    private TextView mTips;
    private DeviceAdapter mBleDevAdapter;
    private BluetoothGatt mBluetoothGatt;
    private boolean isConnected = false;
    private final DeviceAdapter mAdapter = new DeviceAdapter();

    public BleClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ble_client, container, false);
        rv = v.findViewById(R.id.rv_ble);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(mAdapter);
    }

    private final OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, Object o, int position) {

        }

        @Override
        public void onItemLongClick(View view, Object o, int position) {

        }
    };
}
