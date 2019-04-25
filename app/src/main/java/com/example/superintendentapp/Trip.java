package com.example.superintendentapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trip {


    private String tripID;
    private String startAddress;
    private String endAddress;
    private String startTime;
    private String endTime;
    private Double duration;
    private Double distance;

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public ArrayList<ArrayList<Double>> getTripCoord() {
        return tripCoord;
    }

    public void setTripCoord(ArrayList<ArrayList<Double>> tripCoord) {
        this.tripCoord = tripCoord;
    }

    private ArrayList<ArrayList<Double>> tripCoord;

    public Trip(String trip, String sAdd, String eAdd, String sTime, String eTime, Double du, Double dis){
        tripID = trip;
        startAddress = sAdd;
        endAddress = eAdd;
        startTime = sTime;
        endTime = eTime;
        duration = du;
        distance = dis;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}

