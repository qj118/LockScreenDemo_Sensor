package com.demon.lockscreendemo;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{

    Button mEnableButton;
    boolean mFlag = false;

    private static final String TAG = "LockScreen";
    Sensor mProximity;
    SensorManager mSensorManager;
    SensorEventListener mSensorEventListener;
    DevicePolicyManager mDPM;
    ComponentName mDeviceComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEnableButton = (Button)findViewById(R.id.enable);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.i(TAG, "onSensorChanged...");
                float thisVal = event.values[0];
                Log.i(TAG, ((Float)thisVal).toString());
                if(thisVal == 0.0)
                {
                    Log.i(TAG, "The distance is less than target.");
                    boolean active = mDPM.isAdminActive(mDeviceComponentName);
                    if(active)
                    {
                        Log.i(TAG, "Authorized");
                        mDPM.lockNow();
                    }
                    else{
                        Log.i(TAG, "Unauthorized");
                        activeManage();
                        mDPM.lockNow();
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceComponentName = new ComponentName(this, AdminReceiver.class);

        mEnableButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!mFlag) {
                    mFlag = true;
                    mEnableButton.setText(R.string.disable);
                    mSensorManager.registerListener(mSensorEventListener, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
                }
                else
                {
                    mFlag = false;
                    mEnableButton.setText(R.string.enable);
                    mSensorManager.unregisterListener(mSensorEventListener, mProximity);
                }
            }
        });

    }

    private void activeManage()
    {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Other description");
        startActivityForResult(intent, 0);
    }

    private void simulateHome()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            simulateHome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}