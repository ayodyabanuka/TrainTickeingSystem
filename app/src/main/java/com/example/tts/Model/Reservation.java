package com.example.tts.Model;

public class Reservation {

    String id;
    int reservationId;
    Client client;
    Train train;

    Stations startStation,endStation;

    String date;

    public Reservation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Stations getStartStation() {
        return startStation;
    }

    public void setStartStation(Stations startStation) {
        this.startStation = startStation;
    }

    public Stations getEndStation() {
        return endStation;
    }

    public void setEndStation(Stations endStation) {
        this.endStation = endStation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}