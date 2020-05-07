package com.gk.app.bluetooth.main;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.gk.app.bluetooth.R;
import com.gk.app.bluetooth.base.BaseActivity;
import com.gk.app.bluetooth.ble.BleClientFragment;
import com.gk.app.bluetooth.ble.BleServerFragment;
import com.gk.app.bluetooth.cbt.ClassicBtClientFragment;
import com.gk.app.bluetooth.cbt.ClassicBtServerFragment;
import com.gk.app.bluetooth.util.ToastUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 检查蓝牙开关
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            ToastUtil.showToastShort("No Bluetooth hardware or driver found！");
            finish();
            return;
        } else {
            if (!adapter.isEnabled()) {
                //直接开启蓝牙
                adapter.enable();
                //跳转到设置界面
                //startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 112);
            }
        }

        // 检查是否支持BLE蓝牙
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtil.showToastShort("Does not support BLE！");
            finish();
            return;
        }

        // Android 6.0动态请求权限(Build.VERSION_CODES.M)
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_COARSE_LOCATION};
            for (String str : permissions) {
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, 111);
                    break;
                }
            }
        }
    }

    public void classicBtClient(View view) {
//        startActivity(new Intent(this, BtClientActivity.class));
        ContainerActivity.start(this, ClassicBtClientFragment.class.getSimpleName());
    }

    public void classicBtServer(View view) {
//        startActivity(new Intent(this, BtServerActivity.class));
        ContainerActivity.start(this, ClassicBtServerFragment.class.getSimpleName());
    }

    public void bleClient(View view) {
//        startActivity(new Intent(this, BleClientActivity.class));
        ContainerActivity.start(this, BleClientFragment.class.getSimpleName());
    }

    public void bleServer(View view) {
//        startActivity(new Intent(this, BleServerActivity.class));
        ContainerActivity.start(this, BleServerFragment.class.getSimpleName());
    }
}
