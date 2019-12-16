package com.example.geoto;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

public class Thermometer {
    private static final String TAG = Thermometer.class.getSimpleName();
    private long mSamplingRateInMSecs;
    private long mSamplingRateNano;
    private SensorEventListener mTemperatureListener = null;
    private SensorManager mSensorManager;
    private Sensor mThermometerSensor;
    private long timePhoneWasLastRebooted = 0;
    private long THERMOMETER_READING_FREQUENCY = 20000;
    private long lastReportTime = 0;
    private float temperatureValue;

    public Thermometer(Context context) {
        // http://androidforums.com/threads/how-to-get-time-of-last-system-boot.548661/
        timePhoneWasLastRebooted = System.currentTimeMillis() -
                SystemClock.elapsedRealtime();
        mSamplingRateNano = (long) (THERMOMETER_READING_FREQUENCY) * 1000000;
        mSamplingRateInMSecs = (long) THERMOMETER_READING_FREQUENCY;
        mSamplingRateInMSecs = (long) THERMOMETER_READING_FREQUENCY;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mThermometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        initThermometerListener();
    }

    /**
     * it inits the listener and establishes the actions to take when a reading is available
     */
    private void initThermometerListener() {
        if (!standardTemperatureSensorAvailable()) {
            Log.d(TAG, "Standard Thermometer unavailable");
        } else {
            Log.d(TAG, "Using Thermometer");
            mTemperatureListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    long diff = event.timestamp - lastReportTime;
// the following operation avoids reporting too many events too quickly - the sensor may always
                    // misbehave and start sending data very quickly
                    if (diff >= mSamplingRateNano) {
                        // see next slide
                        long actualTimeInMseconds = timePhoneWasLastRebooted + (long) (event.timestamp / 1000000.0);
                        temperatureValue = event.values[0];
                        int accuracy = event.accuracy;
                        Log.i(TAG, mSecsToString(actualTimeInMseconds) + ": current temperature: " +
                                temperatureValue + "with accuracy: " + accuracy);
                        lastReportTime = event.timestamp;
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
        }
    }

    public float getTempValue(){
        return temperatureValue;
    }

    private String mSecsToString(long actualTimeInMseconds) {
        return "NO";
    }

    public boolean standardTemperatureSensorAvailable() {
        return (mThermometerSensor != null);
    }
    /** * it starts the pressure monitoring */
    public void startSensingTemperature() {
        // if the sensor is null,then mSensorManager is null and we get a crash
        if (standardTemperatureSensorAvailable()) {
            Log.d("Standard Thermometer", "starting listener");
            // delay is in microseconds (1millisecond=1000 microseconds)
            // it does not seem to work though
            //stopThermometer();
            // otherwise we stop immediately because
            mSensorManager.registerListener(mTemperatureListener, mThermometerSensor,
                    (int) (mSamplingRateInMSecs * 1000));
        } else {
            Log.i(TAG, "Thermometer unavailable or already active");}}
    /** * this stops the Thermometer */
    public void stopThermometer() {
        if (standardTemperatureSensorAvailable()) {
            Log.d("Standard Thermometer", "Stopping listener");
            try {
                mSensorManager.unregisterListener(mTemperatureListener);
            } catch (Exception e) {
                // probably already unregistered
            }
        }
    }
}
