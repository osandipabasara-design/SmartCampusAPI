// Name: Osandi Randeniya
// UOW ID: w2153603
// IIT ID: 20242020

package com.smartcampus.exception;

// This error happens when trying to delete a room that still has sensors
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String roomId) {
        super("Room " + roomId + " cannot be deleted: it still has sensors assigned to it.");
    }
}
