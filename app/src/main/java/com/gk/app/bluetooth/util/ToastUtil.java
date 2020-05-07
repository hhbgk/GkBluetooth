package com.gk.app.bluetooth.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ToastUtil {
    private static String tag = ToastUtil.class.getSimpleName();

    private static WeakReference<Context> contextWeakReference;

    public static void init(Context context) {
        if(context == null) return;
        contextWeakReference = new WeakReference<>(context.getApplicationContext());
    }

    @SuppressLint("ShowToast")
    private static void showToast(String msg, int duration) {
        if (contextWeakReference == null) {
            throw new RuntimeException("u have not init toast utils");
        }
        if (contextWeakReference.get() == null) {
            GLog.e(tag, "contextWeakReference.get is null ");
            return;
        }
        if (!TextUtils.isEmpty(msg) && duration >= 0) {
            Toast mToast;
//            if (mToast == null) {
                mToast = Toast.makeText(contextWeakReference.get(), msg, duration);
//            } else {
                mToast.setText(msg);
                mToast.setDuration(duration);
//            }
            mToast.show();
        } else {
            throw new IllegalArgumentException("duration " + duration);
        }
    }

    public static void showToastShort(String info) {
        showToast(info, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }
}
