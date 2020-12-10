package net.leelink.healthdoctor.adapter;

import android.view.View;

public interface OnSignListener {
    void onItemClick(View view);
    void onButtonClick(View view, int position);
    void onButtonCancel(View view,int position);
}
