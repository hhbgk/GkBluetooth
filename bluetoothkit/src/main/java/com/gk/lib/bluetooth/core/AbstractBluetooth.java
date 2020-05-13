package com.gk.lib.bluetooth.core;

import com.gk.lib.bluetooth.IBluetooth;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-11
 * UpdateRemark:
 */
public abstract class AbstractBluetooth implements IBluetooth {
    protected String tag = getClass().getSimpleName();
}
