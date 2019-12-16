package com.example.geoto;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

import java.util.Date;

public class Barometer {
    private static final String TAG = Barometer.class.getSimpleName();
    private long mSamplingRateInMSecs;
    private long mSamplingRateNano;
    private SensorEventListener mPressureListener = null;
    private SensorManager mSensorManager;
    private Sensor mBarometerSensor;
    private long timePhoneWasLastRebooted = 0;
    private long BAROMETER_READING_FREQUENCY = 20000;
    private long lastReportTime = 0;
    private Date date;

    public Barometer(Context context) {
        // http://androidforums.com/threads/how-to-get-time-of-last-system-boot.548661/
        timePhoneWasLastRebooted = System.currentTimeMillis() -
                SystemClock.elapsedRealtime();
        mSamplingRateNano = (long) (BAROMETER_READING_FREQUENCY) * 1000000;
        mSamplingRateInMSecs = (long) BAROMETER_READING_FREQUENCY;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mBarometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        initBarometerListener();
    }

    /**
     * it inits the listener and establishes the actions to take when a reading is available
     */
    private void initBarometerListener() {
        if (!standardPressureSensorAvailable()) {
            Log.d(TAG, "Standard Barometer unavailable");
        } else {
            Log.d(TAG, "Using Barometer");
            mPressureListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    long diff = event.timestamp - lastReportTime;
// the following operation avoids reporting too many events too quickly - the sensor may always
                    // misbehave and start sending data very quickly
                    if (diff >= mSamplingRateNano) {
                        // see next slide
                        long actualTimeInMseconds = timePhoneWasLastRebooted + (long) (event.timestamp / 1000000.0);
                        float pressureValue = event.values[0];
                        int accuracy = event.accuracy;
                        Log.i(TAG, mSecsToString(actualTimeInMseconds) + ": current barometric pressure: " +
                                pressureValue + "with accuract: " + accuracy);
                        lastReportTime = event.timestamp;
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
        }
    }

    private String mSecsToString(long actualTimeInMseconds) {
        return "NO";
    }

    public boolean standardPressureSensorAvailable() {
        return (mBarometerSensor != null);
    }
    /** * it starts the pressure monitoring */
    public void startSensingPressure() {
        // if the sensor is null,then mSensorManager is null and we get a crash
        if (standardPressureSensorAvailable()) {
            Log.d("Standard Barometer", "starting listener");
            // delay is in microseconds (1millisecond=1000 microseconds)
            // it does not seem to work though
            //stopBarometer();
            // otherwise we stop immediately because
            mSensorManager.registerListener(mPressureListener, mBarometerSensor,
                    (int) (mSamplingRateInMSecs * 1000));
        } else {
            Log.i(TAG, "barometer unavailable or already active");}}
    /** * this stops the barometer */
    public void stopBarometer() {
        if (standardPressureSensorAvailable()) {
            Log.d("Standard Barometer", "Stopping listener");
            try {
                mSensorManager.unregisterListener(mPressureListener);
            } catch (Exception e) {
                // probably already unregistered
            }
        }
    }
}
