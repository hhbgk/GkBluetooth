package com.gk.app.bluetooth.ble;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gk.app.bluetooth.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BleClientFragment extends Fragment {

    public BleClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ble_client, container, false);
    }
}
