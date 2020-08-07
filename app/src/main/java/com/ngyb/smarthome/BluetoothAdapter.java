package com.ngyb.smarthome;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/8/7 09:53
 */
public class BluetoothAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BluetoothDevice> lists;
    private static final String TAG = "BluetoothAdapter";

    public BluetoothAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (lists == null || lists.size() == 0) {
            return 0;
        }
        return lists.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item, null);
        }
        TextView tv1 = convertView.findViewById(R.id.tv1);
        TextView tv2 = convertView.findViewById(R.id.tv2);
        if (getItem(position) != null) {
            tv1.setText(getItem(position).getName());
            tv2.setText(getItem(position).getAddress());
        }
        return convertView;
    }

    public void setData(ArrayList<BluetoothDevice> lists) {
        if (this.lists != null) {
            this.lists.clear();
        }else{
            this.lists = new ArrayList<>();
        }
        this.lists.addAll(lists);
        Log.e(TAG, "setData: " + this.lists.size());
    }
}
