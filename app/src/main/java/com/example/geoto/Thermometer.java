package com.example.geoto;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.Date;

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
    private Date date;
    private NewVisitFragment parent;

    /**
     * Barometer constructor setting frequencies and setting sensors
     * @param context the environment the application is running in
     * @param parent the parent class thermometer is to be used by
     */
    public Thermometer(Context context, NewVisitFragment parent) {
        this.parent = parent;
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
                        date = new Date();
                        parent.storeTemp(temperatureValue, date);
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

    /**
     * gets the current pressure value
     * @return pressure value
     */
    public float getTempValue(){
        return temperatureValue;
    }

    /**
     * Turns miliseconds to string
     * @param actualTimeInMseconds the time in miliseconds
     * @return
     */
    private String mSecsToString(long actualTimeInMseconds) {
        return String.valueOf(actualTimeInMseconds);
    }

    /**
     * get date of reading
     * @return date
     */
    public Date getDate(){
        return date;
    }


    /**
     * Check if the sensor is not null
     * @return true if sensor is not null
     */
    public boolean standardTemperatureSensorAvailable() {
        return (mThermometerSensor != null);
    }

    /**
     * it starts the temperature monitoring
     */
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


    /**
     * this stops the Thermometer
     */
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
