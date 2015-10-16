package com.elegion.nbumakov.customviewexample.util;

import android.util.Log;

/**
 * The class provides a smooth rotation of compass
 *
 * @author Nikita Bumakov
 */
public class SmoothCompassThread extends Thread implements IBearingAware {

    private static final String TAG = SmoothCompassThread.class.getCanonicalName();

    private static final int LONG_SLEEP = 120;
    private static final int DEFAULT_SLEEP = 40;

    private static final float ARRIVED_EPS = 0.65f;
    private static final float LEAVED_EPS = 2.5f;
    private static final float SPEED_EPS = 0.55f;

    private IBearingAware mCompassView;

    private float mGoalDirection = 0;
    private boolean mIsRunning = false;

    public SmoothCompassThread(IBearingAware compassView) {
        mCompassView = compassView;
    }

    public void setRunning(boolean isRunning) {
        mIsRunning = isRunning;
        if(mIsRunning){
            start();
        }
    }

    @Override
    public void run() {
        float speed = 0;
        float needleDirection = mGoalDirection;
        boolean isArrived = false; // The needle has not arrived the mGoalDirection

        while (mIsRunning) {
            float currentDirection = mGoalDirection;
            boolean needPainting = isNeedPainting(isArrived, speed, needleDirection, currentDirection);

            if (needPainting) {
                isArrived = false;
                float difference = CompassHelper.calculateNormalDifference(needleDirection, currentDirection);
                speed = calculateSpeed(difference, speed);
                currentDirection = needleDirection + speed;
                needleDirection = currentDirection;
                mCompassView.updateBearing(needleDirection);
            } else {
                isArrived = true;
            }
            try {
                if (isArrived) {
                    Thread.sleep(LONG_SLEEP);
                } else {
                    Thread.sleep(DEFAULT_SLEEP);
                }
            } catch (InterruptedException e) {
                Log.w(TAG, "interrupt() was called for SmoothCompassThread while it was sleeping", e);
            }
        }
    }

    private float calculateSpeed(float difference, float oldSpeed) {
        difference = difference / 4;
        oldSpeed = oldSpeed * 0.75f;
        oldSpeed += difference / 25.0f;
        return oldSpeed;
    }

    private boolean isNeedPainting(boolean isArrived, float speed, float needleDirection, float goalDirection) {
        if (isArrived) {
            return Math.abs(needleDirection - goalDirection) > LEAVED_EPS;
        } else {
            return Math.abs(needleDirection - goalDirection) > ARRIVED_EPS || Math.abs(speed) > SPEED_EPS;
        }
    }

    @Override
    public void updateBearing(float bearing) {
        mGoalDirection = bearing;
    }
}
