/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi.storage;

import com.smartcampus.smartcampusapi.model.Room;
import com.smartcampus.smartcampusapi.model.Sensor;
import com.smartcampus.smartcampusapi.model.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author osandirandeniya
 */
public class DataStore {
    
    // All rooms stored by their ID
    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();

    // All sensors stored by their ID
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    // All readings stored per sensor ID
    public static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
    
}
