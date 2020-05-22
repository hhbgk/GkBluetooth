package com.gk.lib.bluetooth.callback;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-22
 * UpdateRemark:
 */
public interface OnCommunicationCallback extends OnReceivedCallback{
    void onSent(byte[] data);
}
