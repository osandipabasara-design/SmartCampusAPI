/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi.exception;

/**
 *
 * @author osandirandeniya
 */
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException() {
        super("Room still has sensors assigned to it.");
    }
}
