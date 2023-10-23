package com.example.tts.Model;

import java.lang.reflect.Array;
import java.util.List;

public class StationsList {
    private List<String> stations;

    public StationsList(List<String> stations) {
        this.stations = stations;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }
}
