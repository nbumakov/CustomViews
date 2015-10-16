package com.elegion.nbumakov.customviewexample.util;

import java.util.Random;

/**
 * @author Nikita Bumakov
 */
public class FakeCompassManager implements Runnable {

    private float mBearing = 0;
    private Random mRandom = new Random();
    private final Object mLock = new Object();

    private IBearingAware mListener;

    public void setListener(IBearingAware listener) {
        synchronized (mLock) {
            mListener = listener;
            if (mListener != null) {
                new Thread(this).start();
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            synchronized (mLock) {
                if (mListener != null) {
                    mBearing += mRandom.nextFloat() > 0.05f
                            ? rnd(5)
                            : rnd(150);

                    mListener.updateBearing(mBearing);
                }else {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private float rnd(int border) {
        return 2 * border * (mRandom.nextFloat() - 0.5f);
    }
}
