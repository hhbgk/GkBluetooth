package com.gk.app.bluetooth.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gk.app.bluetooth.R;
import com.gk.app.bluetooth.base.BaseActivity;
import com.gk.app.bluetooth.ble.BleClientFragment;
import com.gk.app.bluetooth.ble.BleServerFragment;
import com.gk.app.bluetooth.cbt.ClassicBtClientFragment;
import com.gk.app.bluetooth.cbt.ClassicBtServerFragment;
import com.gk.app.bluetooth.util.GLog;

public class ContainerActivity extends BaseActivity {
    private String tag = getClass().getSimpleName();

    public static void start(Context context, String target) {
        Intent intent = new Intent(context, ContainerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("target", target);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            String targetName = bundle.getString("target");
            gotoTargetFragment(targetName);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void gotoTargetFragment(String targetName) {
        Fragment fragment = null;
        if (ClassicBtClientFragment.class.getSimpleName().equals(targetName)) {
            fragment = getSupportFragmentManager().findFragmentByTag(ClassicBtClientFragment.class.getSimpleName());
            if(fragment == null){
                fragment = new ClassicBtClientFragment();
            }
        } else if (ClassicBtServerFragment.class.getSimpleName().equals(targetName)) {
            fragment = getSupportFragmentManager().findFragmentByTag(ClassicBtServerFragment.class.getSimpleName());
            if(fragment == null){
                fragment = new ClassicBtServerFragment();
            }
        } else if (BleClientFragment.class.getSimpleName().equals(targetName)) {
            fragment = getSupportFragmentManager().findFragmentByTag(BleClientFragment.class.getSimpleName());
            if(fragment == null){
                fragment = new BleClientFragment();
            }
        } else if (BleServerFragment.class.getSimpleName().equals(targetName)) {
            fragment = getSupportFragmentManager().findFragmentByTag(BleServerFragment.class.getSimpleName());
            if(fragment == null){
                fragment = new BleServerFragment();
            }
        }
        if(!TextUtils.isEmpty(targetName)) {
            changeFragment(fragment, targetName);
        }else{
            GLog.e(tag, "Target name cannot be null");
        }
    }

    private void changeFragment(Fragment fragment, String fragmentTag) {
        if (fragment != null && !isFinishing() && !isDestroyed()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (!TextUtils.isEmpty(fragmentTag)) {
                fragmentTransaction.replace(R.id.container, fragment, fragmentTag);
            } else {
                fragmentTransaction.replace(R.id.container, fragment);
            }
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
