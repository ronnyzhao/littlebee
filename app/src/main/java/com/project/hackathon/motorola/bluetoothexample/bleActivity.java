package com.project.hackathon.motorola.bluetoothexample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class bleActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothGatt    mGatt;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private static final String BLE_TAG = "BEE_BLE_LOG";
    Context mCtx;

    private boolean mScanning;
    private Handler mHandler;


    private UUID myUUID;
    private final String BEE_BLESERVICE_UUID =
            "00000000-000F-11E1-9AB4-0002A5D5C51";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        mCtx = this;
        mHandler = new Handler();

        // obtaints the ble adapter
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(mCtx.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        scanLeDevice(true);
    }


    //BLE scan callback
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(BLE_TAG, "Device found is:" + device.getAddress());
                            Log.d(BLE_TAG, "Device found is:" + device.getName());

                            // if device is a littleBee gather this device
                            if(new String("LB").equals(device.getName())) {
                                // gets the device instance and connect to it
                                mGatt = device.connectGatt(mCtx, true, mGattCallback);
                            }

                        }
                    });
                }
            };



    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);

                    if(newState == BluetoothProfile.STATE_DISCONNECTED){
                        Log.d(BLE_TAG, "LittleBee was disconected! restarting the advertisement");
                        scanLeDevice(true);

                    } else if(newState == BluetoothProfile.STATE_CONNECTED) {
                        Log.d(BLE_TAG, "LittleBee is connected! ");
                    }

                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status){
                    super.onServicesDiscovered(gatt, status);
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                }
            };


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

}
