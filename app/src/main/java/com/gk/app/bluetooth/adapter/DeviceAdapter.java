package com.gk.app.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gk.app.bluetooth.R;
import com.gk.app.bluetooth.listener.OnItemClickListener;
import com.gk.app.bluetooth.main.App;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-7
 * UpdateRemark:
 */
public final class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyHolder> {
    private final List<BluetoothDevice> mDevices = new ArrayList<>();
    private Context context;
    private OnItemClickListener<BluetoothDevice> onItemClickListener;

    public DeviceAdapter() {
        context = App.getApplication();
    }

    @NonNull
    @Override
    public DeviceAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new MyHolder(v, mDevices, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.MyHolder holder, int position) {
        BluetoothDevice device = mDevices.get(position);
        if (!TextUtils.isEmpty(device.getName())) {
            holder.name.setText(device.getName());
        }
        if (!TextUtils.isEmpty(device.getAddress())) {
            boolean bondState = device.getBondState() == BluetoothDevice.BOND_BONDED;
            String state = bondState ? context.getString(R.string.bonded) : context.getString(R.string.not_bonded);
            String s = String.format("%s (%s)", device.getAddress(), state);
            holder.address.setText(s);
        }
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView name;
        final TextView address;
        final OnItemClickListener listener;
        final List<BluetoothDevice> devices;

        MyHolder(View itemView, List<BluetoothDevice> devices, OnItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            this.devices = devices;
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            if (pos >= 0 && pos < devices.size()) {
                listener.onItemClick(v, devices.get(pos), pos);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener<BluetoothDevice> listener) {
        onItemClickListener = listener;
    }

    public void addAll(Collection<? extends BluetoothDevice> devices) {
        mDevices.addAll(mDevices.size(), devices);
    }

    public void add(BluetoothDevice dev) {
        if (mDevices.contains(dev)) return;
        mDevices.add(mDevices.size(), dev);
    }
}
