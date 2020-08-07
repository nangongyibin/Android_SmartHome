package com.ngyb.smarthome;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/8/7 09:37
 */
public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "BluetoothActivity";
    private ListView lv;
    private MyReceiver myReceiver;
    private BluetoothAdapter adapter;
    private ArrayList<BluetoothDevice> lists = new ArrayList<>();
    private com.ngyb.smarthome.BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        init();
    }

    private void init() {
        initView();
        initReceiver();
        initAdapter();
    }

    private void initAdapter() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        lv.setOnItemClickListener(this);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, intentFilter);
    }

    private void initView() {
        lv = findViewById(R.id.lv);
    }

    /**
     * @param view 打开蓝牙
     */
    public void open(View view) {
        if (adapter.isEnabled()) {
            return;
        }
        adapter.enable();
        Toast.makeText(this, "打开蓝牙", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param view 关闭蓝牙
     */
    public void close(View view) {
        if (!adapter.isEnabled()) {
            return;
        }
        adapter.disable();
        Toast.makeText(this, "关闭蓝牙", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param view 开始扫描
     */
    public void start(View view) {
        if (!adapter.isDiscovering()) {
            adapter.startDiscovery();
            //需要清空list列表中已有的数据
            if (this.lists != null && this.lists.size() > 0) {
                this.lists.clear();
            } else {
                this.lists = new ArrayList<>();
            }
            if (bluetoothAdapter == null) {
                bluetoothAdapter = new com.ngyb.smarthome.BluetoothAdapter(BluetoothActivity.this);
            }
            if (lv.getAdapter() == null) {
                bluetoothAdapter.setData(lists);
                lv.setAdapter(bluetoothAdapter);
            } else {
                bluetoothAdapter.setData(lists);
                bluetoothAdapter.notifyDataSetChanged();
            }
            Toast.makeText(this, "开始扫描", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "蓝牙已经在扫描中。。。。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param view 停止扫描
     */
    public void stop(View view) {
        if (adapter.isDiscovering()) {
            adapter.cancelDiscovery();
            Toast.makeText(this, "关闭蓝牙", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //连接处理
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())) {
                Toast.makeText(context, "开始扫描", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                Toast.makeText(context, "扫描完成", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice data = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (lists == null) {
                    Log.e(TAG, "onReceive: " + new Gson().toJson(data));
                    lists = new ArrayList<>();
                }
                lists.add(data);
                Log.e(TAG, "onReceive: " + lists.size());
                if (bluetoothAdapter == null) {
                    bluetoothAdapter = new com.ngyb.smarthome.BluetoothAdapter(BluetoothActivity.this);
                }
                if (lv.getAdapter() == null) {
                    Log.e(TAG, "onReceive111: " + lists.size());
                    bluetoothAdapter.setData(lists);
                    lv.setAdapter(bluetoothAdapter);
                } else {
                    Log.e(TAG, "onReceive2222: " + lists.size());
                    bluetoothAdapter.setData(lists);
                    bluetoothAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
