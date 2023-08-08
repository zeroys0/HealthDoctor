package net.leelink.healthdoctor.im.adapter;

import android.view.View;

public interface OnMessageListener {
    void onItemClick(View view);
    void onButtonClick(View view, int position);
    void onMessageClick(View view, int position);
}
