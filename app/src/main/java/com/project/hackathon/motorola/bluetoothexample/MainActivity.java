package com.project.hackathon.motorola.bluetoothexample;

import android.Manifest;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {


    LineGraphSeries<DataPoint> mSeries ;
    GraphView mBeeGraph;
    private Handler  mGraphHandler;
    private Runnable mGraphTimer;
    private int xpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBeeGraph = (GraphView)findViewById(R.id.graph);
        mGraphHandler = new Handler();

        mSeries =  new LineGraphSeries<>();
        mSeries.setTitle("Bee Hissing Level");
        mSeries.setBackgroundColor(Color.GRAY);

        mBeeGraph.addSeries(mSeries);

        Viewport vp = mBeeGraph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(1000);


        xpos = 0;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();

        mGraphTimer = new Runnable() {
            @Override
            public void run() {

                xpos++;
                mSeries.appendData(new DataPoint(xpos, bleActivity.aggroLevel), false, 1000);
                mGraphHandler.postDelayed(this, 100);
            }
        };

        mGraphHandler.postDelayed(mGraphTimer, 100);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}