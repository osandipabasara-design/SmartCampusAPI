// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

package com.smartcampus.exception;

// This error happens when a sensor is in maintenance mode
public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException(String sensorId) {
        super("Sensor " + sensorId + " is under MAINTENANCE and cannot accept new readings.");
    }
}
