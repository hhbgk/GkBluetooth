package com.gk.lib.bluetooth.util;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {
    private static Handler mHandler;

    private static void createHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    public static void post(Runnable runnable) {
        postDelayed(runnable, 0);
    }

    public static void postDelayed(Runnable runnable, long delayInMillis) {
        createHandler();
        mHandler.postDelayed(runnable, delayInMillis);
    }

    public static void remove(Runnable runnable) {
        createHandler();
        mHandler.removeCallbacks(runnable);
    }
}
