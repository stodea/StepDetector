package com.example.stepdetector;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 0.08f;

    private boolean isMoving = false;
    private boolean sent = false;

    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float acceleration = (float) Math.abs(Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH);

        Date currentTime = Calendar.getInstance().getTime();
        String pattern = "HH:mm:ss:SSS";
        String patternMiliseconds = "SSS";

        DateFormat df = new SimpleDateFormat(pattern);
        String currentTimeAsString = df.format(currentTime);

        DateFormat dff = new SimpleDateFormat(patternMiliseconds);
        String currentTimeMiliseconds = dff.format(currentTime);

        if (Integer.valueOf(currentTimeMiliseconds) < 200){
            if (!sent){
//                new SendSignalTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "192.168.224.36", "12345", currentTimeAsString + " " + acceleration + " " + x + " " + y + " " + z + " period");
//                sent = true;
                if (acceleration >= SHAKE_THRESHOLD) {
                    if (!isMoving){
                        new SendSignalTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "192.168.224.36", "12345", currentTimeAsString + " " + acceleration + " " + x + " " + y + " " + z + " press");
                        sent = true;
                        isMoving = true;
                        check = 0;
                    }
                } else if (acceleration < SHAKE_THRESHOLD) {
                    check = check + 1;
                    if (check == 2 && isMoving){
                        new SendSignalTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "192.168.224.36", "12345", currentTimeAsString + " " + acceleration + " " + x + " " + y + " " + z + " release");
                        check = 0;
                        isMoving = false;
                        sent = true;
                    }
                }
            }

        }
        else {
            sent = false;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }
}