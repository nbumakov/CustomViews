package com.elegion.nbumakov.customviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elegion.nbumakov.customviewexample.util.FakeCompassManager;
import com.elegion.nbumakov.customviewexample.util.SmoothCompassThread;

/**
 * @author Nikita Bumakov
 */
public class CompassActivity extends AppCompatActivity {

    private FakeCompassManager mCompassManager = new FakeCompassManager();
    private SmoothCompassThread mSmoothCompassThread;

    private CompassView mCompassView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mCompassView = (CompassView) findViewById(R.id.compass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSmoothCompassThread = new SmoothCompassThread(mCompassView);
        mSmoothCompassThread.setRunning(true);
        mCompassManager.setListener(mSmoothCompassThread);
    }

    @Override
    protected void onPause() {
        mSmoothCompassThread.setRunning(false);
        mSmoothCompassThread = null;
        mCompassManager.setListener(null);
        super.onPause();
    }
}
