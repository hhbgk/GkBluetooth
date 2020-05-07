package com.gk.app.bluetooth.cbt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gk.app.bluetooth.R;
import com.gk.app.bluetooth.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassicBtServerFragment extends BaseFragment {

    public ClassicBtServerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classic_bt_server, container, false);
    }
}
