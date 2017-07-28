package com.example.bipl.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener {
    private Button btn_start,btn_stop;
    private TextView tv_x,tv_y;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Socket s;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start=(Button)findViewById(R.id.button);
        btn_stop=(Button)findViewById(R.id.button2);
        tv_x=(TextView)findViewById(R.id.textView2);
        tv_y=(TextView)findViewById(R.id.textView3);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if(event.sensor.getType()  == Sensor.TYPE_ACCELEROMETER) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    tv_x.post(new Runnable() {
                        @Override
                        public void run() {
                            if (event.values[0] > 0.001 && event.values[0]<11 && event.values[1] > 0.001 && event.values[1]<11) {
                                tv_x.setText("X: "+String.valueOf(event.values[0]));
                                //sendLocation(s, event.values[0]);
                                //sendLocation(s1, event.values[1]);
                            }
                        }
                    });

                    tv_y.post(new Runnable() {
                        @Override
                        public void run() {
                            if (event.values[0] > 0.001 && event.values[0]<11 && event.values[1] > 0.001 && event.values[1]<11) {
                                tv_y.setText("Y: "+String.valueOf(event.values[1]));
                                //sendLocation(s, event.values[0]);
                                //sendLocation(s1, event.values[1]);
                            }
                        }
                    });
                }
            }).start();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button){
            mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);
            /*try {
                s = new Socket("192.168.214.1",7778);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        if(v.getId()==R.id.button2){
            mSensorManager.unregisterListener(this);
            /*try {
                sendLocation(s,"close");
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    public void sendLocation(Socket s,float x_axis,float y_axis){
        try{
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            dout.writeUTF(String.valueOf(x_axis)+","+String.valueOf(y_axis));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendLocation(Socket s,float axis){
        try{
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            dout.writeUTF(String.valueOf(axis));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendLocation(Socket s,String axis){
        try{
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            dout.writeUTF(String.valueOf(axis));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

