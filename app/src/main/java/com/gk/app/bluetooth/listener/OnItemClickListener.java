package com.gk.app.bluetooth.listener;

import android.view.View;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-8
 * UpdateRemark:
 */
public interface OnItemClickListener<T> {
    void onItemClick(View view, T t, int position);

    void onItemLongClick(View view, T t, int position);
}
