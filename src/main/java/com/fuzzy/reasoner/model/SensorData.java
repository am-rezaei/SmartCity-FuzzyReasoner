package com.fuzzy.reasoner.model;

public class SensorData {
    public long timestamp;
    public String source;
    public String home;
    public int val;

    public long getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public String getHome() {
        return home;
    }

    public int getVal() {
        return val;
    }

    public SensorData(long timestamp, String source, String home, int val) {
        this.timestamp = timestamp;
        this.source = source;
        this.home = home;
        this.val = val;
    }

    public SensorData(String home, int val) {
        this.home = home;
        this.val = val;
    }

    public SensorData( ) {
    }
}
