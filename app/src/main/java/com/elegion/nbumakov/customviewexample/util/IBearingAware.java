package com.elegion.nbumakov.customviewexample.util;


/**
 * Interface of activities uses compass sensor
 *
 * @author Grigory Kalabin. grigory.kalabin@gmail.com
 * @since Nov 10, 2010
 */
public interface IBearingAware {
    /**
     * @param bearing
     *         - new bearing in degrees
     */
    void updateBearing(float bearing);
}
